import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;

public class CoreUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ItemBox itemBox;
    private final List<Person> people;
    private final DAO dao;
    private final DefaultTableModel tableModel;

    private final JTextField itemIdField = new JTextField();
    private final JTextField itemNameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JTextField weightField = new JTextField();
    private final JTextField sizeField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField discountField = new JTextField();
    private final JTextField stockField = new JTextField();
    private final JTextField usernameField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Customer", "Seller", "Driver", "Admin"});
    private final JTextField shopNameField = new JTextField();
    private final JTextField licenseField = new JTextField();
    private final JTextField vehicleField = new JTextField();
    private final JTextField queryField = new JTextField();
    private final JTextArea statusArea = new JTextArea(4, 40);

    // user tabs content areas (models and lists for double-click details)
    private final DefaultListModel<Person> customerModel = new DefaultListModel<>();
    private final JList<Person> customerList = new JList<>(customerModel);
    private final DefaultListModel<Person> sellerModel = new DefaultListModel<>();
    private final JList<Person> sellerList = new JList<>(sellerModel);
    private final DefaultListModel<Person> driverModel = new DefaultListModel<>();
    private final JList<Person> driverList = new JList<>(driverModel);

    public CoreUI() {
        super("Courier Management System");
        this.itemBox = new ItemBox();
        this.people = new ArrayList<>();
        this.dao = new DAO("data.json");

        seedData();
        loadFromDisk();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setMinimumSize(new Dimension(980, 720));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Courier Management System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(new Color(33, 47, 61));
        title.setBorder(new EmptyBorder(12, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(12, 12));
        contentPanel.setBorder(new EmptyBorder(8, 12, 12, 12));
        contentPanel.setBackground(new Color(245, 247, 250));

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item details"));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(0, 220));
        addFormFields(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(245, 247, 250));
        JButton addButton = new JButton("Add Item");
        JButton updateButton = new JButton("Update Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton refreshButton = new JButton("Refresh");
        JButton saveButton = new JButton("Save");
        JButton loginButton = new JButton("Login");
        JButton queryButton = new JButton("Query");
        JButton addSellerButton = new JButton("Add Seller");
        JButton addDriverButton = new JButton("Add Driver");
        JButton showUsersButton = new JButton("Show Users");
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(saveButton);
        styleButton(loginButton);
        styleButton(queryButton);
        styleButton(addSellerButton);
        styleButton(addDriverButton);
        styleButton(showUsersButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(addSellerButton);
        buttonPanel.add(addDriverButton);
        buttonPanel.add(showUsersButton);

        JPanel authPanel = new JPanel(new GridBagLayout());
        authPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin login"));
        authPanel.setBackground(Color.WHITE);
        addLoginFields(authPanel);

        JPanel detailsPanel = new JPanel(new BorderLayout(12, 12));
        detailsPanel.setBackground(new Color(245, 247, 250));
        detailsPanel.add(formPanel, BorderLayout.CENTER);
        detailsPanel.add(authPanel, BorderLayout.SOUTH);

        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        queryPanel.setBackground(new Color(245, 247, 250));
        queryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Query inventory"));
        JLabel queryLabel = new JLabel("Search:");
        queryLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        queryField.setPreferredSize(new Dimension(260, 30));
        queryPanel.add(queryLabel);
        queryPanel.add(queryField);
        queryPanel.add(queryButton);

        JPanel topPanel = new JPanel(new BorderLayout(12, 12));
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(detailsPanel, BorderLayout.CENTER);
        topPanel.add(queryPanel, BorderLayout.SOUTH);
        center.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Description", "Price", "Stock", "Discount"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 280));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Inventory table"));
        center.add(scrollPane, BorderLayout.CENTER);

        statusArea.setEditable(false);
        statusArea.setBackground(new Color(248, 249, 251));
        statusArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Status"));
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        center.add(statusArea, BorderLayout.SOUTH);

        // create Users tab with sub-tabs (use JList so double-click opens detail UIs)
        JTabbedPane usersTabs = new JTabbedPane();

        // renderer to show name and govID
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Person) {
                    Person p = (Person) value;
                    setText(p.getName() + " - " + p.getGovID() + " (" + p.getClass().getSimpleName() + ")");
                }
                return this;
            }
        };

        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerList.setCellRenderer(renderer);
        customerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = customerList.getSelectedValue();
                    if (p instanceof Customer) {
                        CustomerUI.show((Customer) p);
                    }
                }
            }
        });
        JScrollPane customerScroll = new JScrollPane(customerList);
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.add(customerScroll, BorderLayout.CENTER);

        sellerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sellerList.setCellRenderer(renderer);
        sellerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = sellerList.getSelectedValue();
                    if (p instanceof Seller) {
                        SellerUI.show((Seller) p);
                    }
                }
            }
        });
        JScrollPane sellerScroll = new JScrollPane(sellerList);
        JPanel sellerPanel = new JPanel(new BorderLayout());
        sellerPanel.add(sellerScroll, BorderLayout.CENTER);

        driverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        driverList.setCellRenderer(renderer);
        driverList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = driverList.getSelectedValue();
                    if (p instanceof Driver) {
                        DriverUI.show((Driver) p);
                    }
                }
            }
        });
        JScrollPane driverScroll = new JScrollPane(driverList);
        JPanel driverPanel = new JPanel(new BorderLayout());
        driverPanel.add(driverScroll, BorderLayout.CENTER);

        usersTabs.addTab("Customers", customerPanel);
        usersTabs.addTab("Sellers", sellerPanel);
        usersTabs.addTab("Drivers", driverPanel);

        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.addTab("Inventory", center);
        mainTabs.addTab("Users", usersTabs);

        contentPanel.add(mainTabs, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addItemFromForm());
        updateButton.addActionListener(e -> updateItemFromForm());
        deleteButton.addActionListener(e -> deleteSelectedItem());
        refreshButton.addActionListener(e -> refreshTable());
        saveButton.addActionListener(e -> saveAll());
        loginButton.addActionListener(e -> login());
        queryButton.addActionListener(e -> queryInventory());
        // user management actions
        addSellerButton.addActionListener(e -> { addSeller(); refreshUserTabs(); });
        addDriverButton.addActionListener(e -> { addDriver(); refreshUserTabs(); });
        showUsersButton.addActionListener(e -> showUserList());

        bindEnterKey(itemIdField, () -> addItemFromForm());
        bindEnterKey(itemNameField, () -> addItemFromForm());
        bindEnterKey(descriptionField, () -> addItemFromForm());
        bindEnterKey(weightField, () -> addItemFromForm());
        bindEnterKey(sizeField, () -> addItemFromForm());
        bindEnterKey(priceField, () -> addItemFromForm());
        bindEnterKey(discountField, () -> addItemFromForm());
        bindEnterKey(stockField, () -> addItemFromForm());
        bindEnterKey(usernameField, () -> login());
        bindEnterKey(passwordField, () -> login());
        bindEnterKey(queryField, () -> queryInventory());

        refreshTable();
    }

    private void seedData() {
        Admin admin = new Admin("A-1001", "System Admin", 35, "Main Office", "077-1234567", "admin@courier.com", "admin", "admin123");
        people.add(admin);
        itemBox.addItem(new Items("IT-100", "Standard Parcel", "Domestic courier parcel", 2, 12, 750f, 10, 25));
        itemBox.addItem(new Items("IT-101", "Express Box", "Priority shipping box", 5, 20, 1300f, 15, 12));
        itemBox.addItem(new Items("IT-102", "Fragile Item", "Glass or electronics packaging", 4, 15, 980f, 8, 18));
    }

    private void loadFromDisk() {
        try {
            DAO localDAO = new DAO("data.json");
            ItemBox loaded = localDAO.loadItemBox();
            List<Person> loadedUsers = localDAO.loadUsers();
            if (!loaded.getAllItems().isEmpty()) {
                itemBox.items.clear();
                itemBox.items.addAll(loaded.getAllItems());
            }
            if (!loadedUsers.isEmpty()) {
                people.clear();
                people.addAll(loadedUsers);
            }
            // refresh user tab displays after loading
            refreshUserTabs();
        } catch (IOException e) {
            statusArea.append("No saved data found. Using defaults.\n");
        }
    }

    private void addFormFields(JPanel formPanel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        int row = 0;
        row = addFieldRow(formPanel, constraints, row, "ID", itemIdField, "Name", itemNameField);
        row = addFieldRow(formPanel, constraints, row, "Description", descriptionField, "Weight", weightField);
        row = addFieldRow(formPanel, constraints, row, "Size", sizeField, "Price", priceField);
        row = addFieldRow(formPanel, constraints, row, "Discount", discountField, "Stock", stockField);
    }

    private void addLoginFields(JPanel authPanel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        addLabeledField(authPanel, constraints, 0, 0, "Username", usernameField, 1.0);
        addLabeledField(authPanel, constraints, 0, 1, "Password", passwordField, 1.0);

        // Role selector
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.15;
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        authPanel.add(roleLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        roleCombo.setPreferredSize(new Dimension(160, 30));
        authPanel.add(roleCombo, constraints);

        // Extra fields for seller/driver
        addLabeledField(authPanel, constraints, 0, 3, "Shop name", shopNameField, 1.0);
        addLabeledField(authPanel, constraints, 0, 4, "License #", licenseField, 1.0);
        addLabeledField(authPanel, constraints, 0, 5, "Vehicle #", vehicleField, 1.0);
    }

    private int addFieldRow(JPanel panel, GridBagConstraints constraints, int row, String leftLabel, JTextField leftField, String rightLabel, JTextField rightField) {
        addLabeledField(panel, constraints, 0, row, leftLabel, leftField, 0.5);
        addLabeledField(panel, constraints, 1, row, rightLabel, rightField, 0.5);
        return row + 1;
    }

    private void addLabeledField(JPanel panel, GridBagConstraints constraints, int column, int row, String labelText, JTextField field, double weight) {
        constraints.gridx = column * 2;
        constraints.gridy = row;
        constraints.weightx = 0.15;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(label, constraints);

        constraints.gridx = column * 2 + 1;
        constraints.weightx = weight;
        field.setPreferredSize(new Dimension(160, 30));
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(field, constraints);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }

    private void bindEnterKey(JTextField field, Runnable action) {
        field.addActionListener(e -> action.run());
    }

    private void queryInventory() {
        String queryText = queryField.getText().trim().toLowerCase();
        if (queryText.isEmpty()) {
            refreshTable();
            statusArea.append("Query cleared. Showing all inventory.\n");
            return;
        }

        tableModel.setRowCount(0);
        for (Items item : itemBox.getAllItems()) {
            String haystack = (item.itemID + " " + item.itemName + " " + item.Description).toLowerCase();
            if (haystack.contains(queryText)) {
                tableModel.addRow(new Object[] {
                    item.itemID,
                    item.itemName,
                    item.Description,
                    item.itemPrice,
                    item.stockCount,
                    item.itemDiscount + "%"
                });
            }
        }
        statusArea.append("Query result: " + queryText + "\n");
    }

    private void addItemFromForm() {
        try {
            String itemId = itemIdField.getText().trim();
            String itemName = itemNameField.getText().trim();
            String description = descriptionField.getText().trim();
            int weight = Integer.parseInt(weightField.getText().trim());
            int size = Integer.parseInt(sizeField.getText().trim());
            float price = Float.parseFloat(priceField.getText().trim());
            int discount = Integer.parseInt(discountField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            Items item = new Items(itemId, itemName, description, weight, size, price, discount, stock);
            itemBox.addItem(item);
            statusArea.append("Added item: " + itemName + "\n");
            clearForm();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for weight, size, price, discount, and stock.", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItemFromForm() {
        String itemId = itemIdField.getText().trim();
        Items existing = itemBox.findItem(itemId);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Item not found. Enter a valid item ID.", "Update failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            existing.itemName = itemNameField.getText().trim();
            existing.Description = descriptionField.getText().trim();
            existing.itemWeight = Integer.parseInt(weightField.getText().trim());
            existing.itemSize = Integer.parseInt(sizeField.getText().trim());
            existing.itemPrice = Float.parseFloat(priceField.getText().trim());
            existing.itemDiscount = Integer.parseInt(discountField.getText().trim());
            existing.stockCount = Integer.parseInt(stockField.getText().trim());
            statusArea.append("Updated item: " + itemId + "\n");
            clearForm();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for all fields.", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedItem() {
        String itemId = itemIdField.getText().trim();
        if (itemId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Provide an item ID to delete.", "Delete item", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean removed = itemBox.removeItem(itemId);
        if (removed) {
            statusArea.append("Deleted item: " + itemId + "\n");
            clearForm();
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "No item with that ID exists.", "Delete item", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        for (Person person : people) {
            if (person.userName.equals(username) && person.password.equals(password)) {
                statusArea.append("Login successful for " + person.name + "\n");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login failed", JOptionPane.ERROR_MESSAGE);
    }

    private void saveAll() {
        try {
            dao.saveData(itemBox, people);
            statusArea.append("Saved system data to data.json\n");
        } catch (IOException e) {
            statusArea.append("Save failed: " + e.getMessage() + "\n");
        }
    }

    private void addSeller() {
        try {
            String govID = JOptionPane.showInputDialog(this, "Gov ID:");
            if (govID == null) return;
            String name = JOptionPane.showInputDialog(this, "Name:");
            if (name == null) return;
            String ageStr = JOptionPane.showInputDialog(this, "Age:");
            if (ageStr == null) return;
            int age = Integer.parseInt(ageStr.trim());
            String address = JOptionPane.showInputDialog(this, "Address:");
            if (address == null) address = "";
            String phone = JOptionPane.showInputDialog(this, "Phone:");
            if (phone == null) phone = "";
            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) email = "";
            String username = JOptionPane.showInputDialog(this, "Username:");
            if (username == null) username = "";
            String password = JOptionPane.showInputDialog(this, "Password:");
            if (password == null) password = "";
            // prefer shop name from form if present
            String shopPrefill = shopNameField.getText() == null ? "" : shopNameField.getText().trim();
            String shopName = JOptionPane.showInputDialog(this, "Shop name:", shopPrefill);
            if (shopName == null) shopName = shopPrefill;
            Seller s = new Seller(govID.trim(), name.trim(), age, address.trim(), phone.trim(), email.trim(), username.trim(), password.trim(), shopName.trim());
            people.add(s);
            statusArea.append("Added seller: " + name + "\n");
            refreshUserTabs();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDriver() {
        try {
            String govID = JOptionPane.showInputDialog(this, "Gov ID:");
            if (govID == null) return;
            String name = JOptionPane.showInputDialog(this, "Name:");
            if (name == null) return;
            String ageStr = JOptionPane.showInputDialog(this, "Age:");
            if (ageStr == null) return;
            int age = Integer.parseInt(ageStr.trim());
            String address = JOptionPane.showInputDialog(this, "Address:");
            if (address == null) address = "";
            String phone = JOptionPane.showInputDialog(this, "Phone:");
            if (phone == null) phone = "";
            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) email = "";
            String username = JOptionPane.showInputDialog(this, "Username:");
            if (username == null) username = "";
            String password = JOptionPane.showInputDialog(this, "Password:");
            if (password == null) password = "";
            // prefer license/vehicle from form if present
            String licensePrefill = licenseField.getText() == null ? "" : licenseField.getText().trim();
            String vehiclePrefill = vehicleField.getText() == null ? "" : vehicleField.getText().trim();
            String license = JOptionPane.showInputDialog(this, "License number:", licensePrefill);
            if (license == null) license = licensePrefill;
            String vehicle = JOptionPane.showInputDialog(this, "Vehicle number:", vehiclePrefill);
            if (vehicle == null) vehicle = vehiclePrefill;
            Driver d = new Driver(govID.trim(), name.trim(), age, address.trim(), phone.trim(), email.trim(), username.trim(), password.trim(), license.trim(), vehicle.trim());
            people.add(d);
            statusArea.append("Added driver: " + name + "\n");
            refreshUserTabs();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUserList() {
        if (people.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available.", "Users", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] names = new String[people.size()];
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            names[i] = p.getName() + " (" + p.getClass().getSimpleName() + ")";
        }
        String selected = (String) JOptionPane.showInputDialog(this, "Select a user:", "Users", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected == null) return;
        int idx = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(selected)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) return;
        Person p = people.get(idx);
        if (p instanceof Seller) {
            SellerUI.show((Seller) p);
        } else if (p instanceof Driver) {
            DriverUI.show((Driver) p);
        } else if (p instanceof Customer) {
            CustomerUI.show((Customer) p);
        } else {
            JOptionPane.showMessageDialog(this, "Name: " + p.getName() + "\nGovID: " + p.getGovID(), "User", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Items item : itemBox.getAllItems()) {
            Object[] row = {
                item.itemID,
                item.itemName,
                item.Description,
                item.itemPrice,
                item.stockCount,
                item.itemDiscount + "%"
            };
            tableModel.addRow(row);
        }
    }

    private void refreshUserTabs() {
        customerModel.clear();
        sellerModel.clear();
        driverModel.clear();
        for (Person p : people) {
            if (p instanceof Seller) {
                sellerModel.addElement(p);
            } else if (p instanceof Driver) {
                driverModel.addElement(p);
            } else if (p instanceof Customer) {
                customerModel.addElement(p);
            }
        }
    }

    private void clearForm() {
        itemIdField.setText("");
        itemNameField.setText("");
        descriptionField.setText("");
        weightField.setText("");
        sizeField.setText("");
        priceField.setText("");
        discountField.setText("");
        stockField.setText("");
    }
}