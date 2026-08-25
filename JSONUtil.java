import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// Small utility to write the same legacy JSON format DAO uses, but without requiring SQLite on the classpath.
public class JSONUtil {
    public static void writeJson(String pathStr, ItemBox itemBox, List<Person> users) throws IOException {
        Path legacyFile = Paths.get(pathStr).toAbsolutePath();
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
            if (i < users.size() - 1) json.append(",");
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
            if (i < items.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}\n");

        Path parent = legacyFile.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(legacyFile, json.toString(), StandardCharsets.UTF_8);
    }

    private static String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
