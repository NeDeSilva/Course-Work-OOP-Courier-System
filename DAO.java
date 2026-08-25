import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private final Path filePath;
    private final String sqliteUrl;

    private static final boolean JDBC_AVAILABLE;
    static {
        boolean ok = true;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            ok = false;
            System.err.println("Warning: SQLite JDBC driver not found on classpath — DAO will fall back to JSON-only mode.");
        }
        JDBC_AVAILABLE = ok;
    }

    public DAO(String filePath) {
        this.filePath = Paths.get(filePath).toAbsolutePath();
        String resolved = this.filePath.toString();
        if (resolved.toLowerCase().endsWith(".json")) {
            resolved = resolved.substring(0, resolved.length() - 5) + ".db";
        }
        this.sqliteUrl = "jdbc:sqlite:" + resolved.replace('\\', '/');
    }

    public void saveData(ItemBox itemBox, List<Person> users) throws IOException {
        if (itemBox == null) {
            throw new IllegalArgumentException("ItemBox cannot be null");
        }
        if (users == null) {
            users = new ArrayList<>();
        }

        try {
            if (!JDBC_AVAILABLE) {
                // SQLite driver unavailable — use JSON-only fallback
                writeLegacyJson(itemBox, users);
                return;
            }
            ensureSchema();
            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false);

                try (PreparedStatement deleteItems = connection.prepareStatement("DELETE FROM items");
                     PreparedStatement deleteUsers = connection.prepareStatement("DELETE FROM users")) {
                    deleteItems.executeUpdate();
                    deleteUsers.executeUpdate();
                }

                try (PreparedStatement insertItem = connection.prepareStatement(
                        "INSERT INTO items (itemID, itemName, description, itemWeight, itemSize, itemPrice, itemDiscount, stockCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    for (Items item : itemBox.getAllItems()) {
                        insertItem.setString(1, item.itemID);
                        insertItem.setString(2, item.itemName);
                        insertItem.setString(3, item.Description);
                        insertItem.setInt(4, item.itemWeight);
                        insertItem.setInt(5, item.itemSize);
                        insertItem.setFloat(6, item.itemPrice);
                        insertItem.setInt(7, item.itemDiscount);
                        insertItem.setInt(8, item.stockCount);
                        insertItem.addBatch();
                    }
                    insertItem.executeBatch();
                }

                try (PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO users (govID, name, age, address, phoneNumber, emailAddress, userName, password, role, shopName, licenseNumber, vehicleNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    for (Person user : users) {
                        insertUser.setString(1, user.govID);
                        insertUser.setString(2, user.name);
                        insertUser.setInt(3, user.age);
                        insertUser.setString(4, user.address);
                        insertUser.setString(5, user.phoneNumber);
                        insertUser.setString(6, user.emailAddress);
                        insertUser.setString(7, user.userName);
                        insertUser.setString(8, user.password);
                        // determine role and extra fields
                        String role = "person";
                        String shopName = "";
                        String licenseNumber = "";
                        String vehicleNumber = "";
                        if (user instanceof Seller) {
                            role = "seller";
                            shopName = ((Seller) user).getShopName();
                        } else if (user instanceof Driver) {
                            role = "driver";
                            licenseNumber = ((Driver) user).getLicenseNumber();
                            vehicleNumber = ((Driver) user).getVehicleNumber();
                        } else if (user.getClass().getSimpleName().equals("Admin")) {
                            role = "admin";
                        } else if (user.getClass().getSimpleName().equals("Customer")) {
                            role = "customer";
                        }
                        insertUser.setString(9, role);
                        insertUser.setString(10, shopName);
                        insertUser.setString(11, licenseNumber);
                        insertUser.setString(12, vehicleNumber);
                        insertUser.addBatch();
                    }
                    insertUser.executeBatch();
                }

                connection.commit();
            }
        } catch (SQLException e) {
            throw new IOException("Failed to save data to SQLite database.", e);
        }

        writeLegacyJson(itemBox, users);
    }

    public ItemBox loadItemBox() throws IOException {
        if (!JDBC_AVAILABLE) {
            // no JDBC driver — load from JSON only
            return loadJsonItemBox();
        }
        ensureSchema();
        ItemBox itemBox = new ItemBox();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT itemID, itemName, description, itemWeight, itemSize, itemPrice, itemDiscount, stockCount FROM items")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                itemBox.addItem(new Items(
                        rs.getString("itemID"),
                        rs.getString("itemName"),
                        rs.getString("description"),
                        rs.getInt("itemWeight"),
                        rs.getInt("itemSize"),
                        rs.getFloat("itemPrice"),
                        rs.getInt("itemDiscount"),
                        rs.getInt("stockCount")));
            }
        } catch (SQLException e) {
            throw new IOException("Failed to load item data from SQLite database.", e);
        }

        if (!itemBox.getAllItems().isEmpty()) {
            return itemBox;
        }

        ItemBox jsonFallback = loadJsonItemBox();
        if (!jsonFallback.getAllItems().isEmpty()) {
            itemBox = jsonFallback;
        }
        return itemBox;
    }

    public List<Person> loadUsers() throws IOException {
        if (!JDBC_AVAILABLE) {
            // no JDBC driver — load users from JSON only
            return loadJsonUsers();
        }
        ensureSchema();
        List<Person> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT govID, name, age, address, phoneNumber, emailAddress, userName, password, role, shopName, licenseNumber, vehicleNumber FROM users")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String govID = rs.getString("govID");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String address = rs.getString("address");
                String phone = rs.getString("phoneNumber");
                String email = rs.getString("emailAddress");
                String username = rs.getString("userName");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String shopName = rs.getString("shopName");
                String licenseNumber = rs.getString("licenseNumber");
                String vehicleNumber = rs.getString("vehicleNumber");

                if (role != null) {
                    switch (role.toLowerCase()) {
                        case "seller":
                            users.add(new Seller(govID, name, age, address, phone, email, username, password, shopName));
                            break;
                        case "driver":
                            users.add(new Driver(govID, name, age, address, phone, email, username, password, licenseNumber, vehicleNumber));
                            break;
                        case "admin":
                            users.add(new Admin(govID, name, age, address, phone, email, username, password));
                            break;
                        case "customer":
                            users.add(new Customer(govID, name, age, address, phone, email, username, password));
                            break;
                        default:
                            users.add(new Person(govID, name, age, address, phone, email, username, password));
                    }
                } else {
                    users.add(new Person(govID, name, age, address, phone, email, username, password));
                }
            }
        } catch (SQLException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (msg.contains("no such column") || msg.contains("no such table") || msg.contains("has no column")) {
                System.err.println("Warning: SQLite users table schema mismatch or missing table/column — falling back to JSON users. (" + e.getMessage() + ")");
                return loadJsonUsers();
            }
            throw new IOException("Failed to load user data from SQLite database.", e);
        }

        if (!users.isEmpty()) {
            return users;
        }

        return loadJsonUsers();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(sqliteUrl);
    }

    private void ensureSchema() throws IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS items ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "itemID TEXT, "
                    + "itemName TEXT, "
                    + "description TEXT, "
                    + "itemWeight INTEGER, "
                    + "itemSize INTEGER, "
                    + "itemPrice REAL, "
                    + "itemDiscount INTEGER, "
                    + "stockCount INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "govID TEXT, "
                    + "name TEXT, "
                    + "age INTEGER, "
                    + "address TEXT, "
                    + "phoneNumber TEXT, "
                    + "emailAddress TEXT, "
                    + "userName TEXT, "
                    + "password TEXT, "
                    + "role TEXT, "
                    + "shopName TEXT, "
                    + "licenseNumber TEXT, "
                    + "vehicleNumber TEXT)");
        } catch (SQLException e) {
            throw new IOException("Unable to initialize SQLite schema.", e);
        }
    }

    private void writeLegacyJson(ItemBox itemBox, List<Person> users) throws IOException {
        // ensure users list is non-null
        if (users == null) users = new java.util.ArrayList<>();
        Path legacyFile = filePath;
        if (legacyFile.toString().toLowerCase().endsWith(".db")) {
            legacyFile = Paths.get(legacyFile.toString().replace(".db", ".json"));
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"users\": [\n");
        for (int i = 0; i < users.size(); i++) {
            Person user = users.get(i);
            json.append("    {\n");
            json.append("      \"govID\": \"").append(escapeJson(user.govID)).append("\",\n");
            json.append("      \"name\": \"").append(escapeJson(user.name)).append("\",\n");
            json.append("      \"age\": ").append(user.age).append(",\n");
            json.append("      \"address\": \"").append(escapeJson(user.address)).append("\",\n");
            json.append("      \"phoneNumber\": \"").append(escapeJson(user.phoneNumber)).append("\",\n");
            json.append("      \"emailAddress\": \"").append(escapeJson(user.emailAddress)).append("\",\n");
            json.append("      \"userName\": \"").append(escapeJson(user.userName)).append("\",\n");
            json.append("      \"password\": \"").append(escapeJson(user.password)).append("\",\n");
            // role and extended fields for Seller/Driver
            String role = "person";
            String shopName = "";
            String licenseNumber = "";
            String vehicleNumber = "";
            if (user instanceof Seller) {
                role = "seller";
                shopName = ((Seller) user).getShopName();
            } else if (user instanceof Driver) {
                role = "driver";
                licenseNumber = ((Driver) user).getLicenseNumber();
                vehicleNumber = ((Driver) user).getVehicleNumber();
            } else if (user.getClass().getSimpleName().equals("Admin")) {
                role = "admin";
            } else if (user.getClass().getSimpleName().equals("Customer")) {
                role = "customer";
            }
            json.append("      \"role\": \"").append(escapeJson(role)).append("\",\n");
            json.append("      \"shopName\": \"").append(escapeJson(shopName)).append("\",\n");
            json.append("      \"licenseNumber\": \"").append(escapeJson(licenseNumber)).append("\",\n");
            json.append("      \"vehicleNumber\": \"").append(escapeJson(vehicleNumber)).append("\"\n");
            json.append("    }");
            if (i < users.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ],\n");

        json.append("  \"items\": [\n");
        List<Items> items = itemBox.getAllItems();
        for (int i = 0; i < items.size(); i++) {
            Items item = items.get(i);
            json.append("    {\n");
            json.append("      \"itemID\": \"").append(escapeJson(item.itemID)).append("\",\n");
            json.append("      \"itemName\": \"").append(escapeJson(item.itemName)).append("\",\n");
            json.append("      \"Description\": \"").append(escapeJson(item.Description)).append("\",\n");
            json.append("      \"itemWeight\": ").append(item.itemWeight).append(",\n");
            json.append("      \"itemSize\": ").append(item.itemSize).append(",\n");
            json.append("      \"itemPrice\": ").append(item.itemPrice).append(",\n");
            json.append("      \"itemDiscount\": ").append(item.itemDiscount).append(",\n");
            json.append("      \"stockCount\": ").append(item.stockCount).append("\n");
            json.append("    }");
            if (i < items.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}\n");

        Path parent = legacyFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(legacyFile, json.toString(), StandardCharsets.UTF_8);
    }

    public void persistUsersJson(ItemBox itemBox, List<Person> users) throws IOException {
        writeLegacyJson(itemBox, users);
    }

    private ItemBox loadJsonItemBox() throws IOException {
        Path jsonPath = filePath;
        if (jsonPath.toString().toLowerCase().endsWith(".db")) {
            jsonPath = Paths.get(jsonPath.toString().replace(".db", ".json"));
        }

        if (!Files.exists(jsonPath)) {
            return new ItemBox();
        }

        String content = Files.readString(jsonPath, StandardCharsets.UTF_8);
        ItemBox itemBox = new ItemBox();
        if (content.trim().isEmpty()) {
            return itemBox;
        }

        String itemsArray = extractArray(content, "items");
        if (itemsArray == null || itemsArray.trim().isEmpty()) {
            return itemBox;
        }

        for (String object : splitObjects(itemsArray)) {
            String itemID = extractString(object, "itemID");
            String itemName = extractString(object, "itemName");
            String description = extractString(object, "Description");
            int weight = extractInt(object, "itemWeight");
            int size = extractInt(object, "itemSize");
            float price = extractFloat(object, "itemPrice");
            int discount = extractInt(object, "itemDiscount");
            int stockCount = extractInt(object, "stockCount");
            itemBox.addItem(new Items(itemID, itemName, description, weight, size, price, discount, stockCount));
        }
        return itemBox;
    }

    private List<Person> loadJsonUsers() throws IOException {
        Path jsonPath = filePath;
        if (jsonPath.toString().toLowerCase().endsWith(".db")) {
            jsonPath = Paths.get(jsonPath.toString().replace(".db", ".json"));
        }

        if (!Files.exists(jsonPath)) {
            return new ArrayList<>();
        }

        String content = Files.readString(jsonPath, StandardCharsets.UTF_8);
        if (content.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Person> users = new ArrayList<>();
        String usersArray = extractArray(content, "users");
        if (usersArray == null || usersArray.trim().isEmpty()) {
            return users;
        }

        for (String object : splitObjects(usersArray)) {
            String govID = extractString(object, "govID");
            String name = extractString(object, "name");
            int age = extractInt(object, "age");
            String address = extractString(object, "address");
            String phoneNumber = extractString(object, "phoneNumber");
            String emailAddress = extractString(object, "emailAddress");
            String userName = extractString(object, "userName");
            String password = extractString(object, "password");
            String role = extractString(object, "role");
            String shopName = extractString(object, "shopName");
            String licenseNumber = extractString(object, "licenseNumber");
            String vehicleNumber = extractString(object, "vehicleNumber");

            // If role missing, try to infer from govID prefix or name heuristics to make login smoother
            if (role == null || role.isEmpty()) {
                if (govID != null) {
                    String up = govID.toUpperCase();
                    if (up.startsWith("A-")) role = "admin";
                    else if (up.startsWith("C-")) role = "customer";
                    else if (up.startsWith("S-")) role = "seller";
                    else if (up.startsWith("D-")) role = "driver";
                }
                if ((role == null || role.isEmpty()) && name != null) {
                    String n = name.toLowerCase();
                    if (n.contains("admin")) role = "admin";
                    else if (n.contains("seller") || n.contains("shop")) role = "seller";
                    else if (n.contains("driver")) role = "driver";
                    else if (n.contains("customer") || n.contains("client")) role = "customer";
                }
                if (role == null) role = "";
            }

            if (role != null && !role.isEmpty()) {
                switch (role.toLowerCase()) {
                    case "seller":
                        users.add(new Seller(govID, name, age, address, phoneNumber, emailAddress, userName, password, shopName));
                        break;
                    case "driver":
                        users.add(new Driver(govID, name, age, address, phoneNumber, emailAddress, userName, password, licenseNumber, vehicleNumber));
                        break;
                    case "admin":
                        users.add(new Admin(govID, name, age, address, phoneNumber, emailAddress, userName, password));
                        break;
                    case "customer":
                        users.add(new Customer(govID, name, age, address, phoneNumber, emailAddress, userName, password));
                        break;
                    default:
                        users.add(new Person(govID, name, age, address, phoneNumber, emailAddress, userName, password));
                }
            } else {
                users.add(new Person(govID, name, age, address, phoneNumber, emailAddress, userName, password));
            }
        }
        return users;
    }

    private static String extractArray(String content, String key) {
        String marker = "\"" + key + "\"";
        int keyIndex = content.indexOf(marker);
        if (keyIndex < 0) {
            return null;
        }
        int openBracket = content.indexOf('[', keyIndex);
        if (openBracket < 0) return null;
        int closeBracket = content.indexOf(']', openBracket);
        if (closeBracket < openBracket) return null;
        return content.substring(openBracket + 1, closeBracket);
    }

    private static List<String> splitObjects(String arrayContent) {
        List<String> objects = new ArrayList<>();
        int braceDepth = 0;
        int start = -1;
        for (int i = 0; i < arrayContent.length(); i++) {
            char ch = arrayContent.charAt(i);
            if (ch == '{') {
                if (braceDepth == 0) {
                    start = i;
                }
                braceDepth++;
            } else if (ch == '}') {
                braceDepth--;
                if (braceDepth == 0 && start >= 0) {
                    objects.add(arrayContent.substring(start + 1, i));
                }
            }
        }
        return objects;
    }

    private static String extractString(String object, String key) {
        String marker = "\"" + key + "\":";
        int keyIndex = object.indexOf(marker);
        if (keyIndex < 0) {
            return "";
        }
        int start = object.indexOf('"', keyIndex + marker.length());
        int end = object.indexOf('"', start + 1);
        if (start < 0 || end < start) {
            return "";
        }
        return unescapeJson(object.substring(start + 1, end));
    }

    private static int extractInt(String object, String key) {
        String marker = "\"" + key + "\":";
        int keyIndex = object.indexOf(marker);
        if (keyIndex < 0) {
            return 0;
        }
        int valueStart = keyIndex + marker.length();
        int valueEnd = object.indexOf(',', valueStart);
        if (valueEnd < 0) {
            valueEnd = object.indexOf('}', valueStart);
        }
        if (valueEnd < 0) {
            valueEnd = object.length();
        }
        String text = object.substring(valueStart, valueEnd).trim();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static float extractFloat(String object, String key) {
        String marker = "\"" + key + "\":";
        int keyIndex = object.indexOf(marker);
        if (keyIndex < 0) {
            return 0f;
        }
        int valueStart = keyIndex + marker.length();
        int valueEnd = object.indexOf(',', valueStart);
        if (valueEnd < 0) {
            valueEnd = object.indexOf('}', valueStart);
        }
        if (valueEnd < 0) {
            valueEnd = object.length();
        }
        String text = object.substring(valueStart, valueEnd).trim();
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}