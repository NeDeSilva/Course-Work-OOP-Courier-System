import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. ABSTRACT BASE CLASS & ENCAPSULATION
// ==========================================
abstract class Person {
    private String id;
    private String name;
    private String phone;

    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public abstract String getRoleDetails();
}

// ==========================================
// 2. INHERITANCE: CUSTOMER & DRIVER
// ==========================================
class Customer extends Person {
    private String address;

    public Customer(String id, String name, String phone, String address) {
        super(id, name, phone);
        this.address = address;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String getRoleDetails() {
        return "Customer Address: " + address;
    }
}

class CourierDriver extends Person {
    private String vehicleNumber;

    public CourierDriver(String id, String name, String phone, String vehicleNumber) {
        super(id, name, phone);
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleNumber() { return vehicleNumber; }

    @Override
    public String getRoleDetails() {
        return "Driver Vehicle: " + vehicleNumber;
    }
}

// ==========================================
// 3. ABSTRACTION & POLYMORPHISM: PARCEL HIERARCHY
// ==========================================
abstract class Parcel {
    private String trackingId;
    private Customer sender;
    private String receiverName;
    private String destination;
    private double weight;
    private String status;

    public Parcel(String trackingId, Customer sender, String receiverName, String destination, double weight) {
        this.trackingId = trackingId;
        this.sender = sender;
        this.receiverName = receiverName;
        this.destination = destination;
        this.weight = weight;
        this.status = "Pending";
    }

    // Encapsulation: Getters & Setters
    public String getTrackingId() { return trackingId; }
    public Customer getSender() { return sender; }
    public String getReceiverName() { return receiverName; }
    public String getDestination() { return destination; }
    public double getWeight() { return weight; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setWeight(double weight) { this.weight = weight; }

    // Polymorphic method to be overridden by subclasses
    public abstract double calculateCost();
    public abstract String getParcelType();
}

// Standard Parcel Subclass
class StandardParcel extends Parcel {
    private static final double RATE_PER_KG = 150.0;

    public StandardParcel(String trackingId, Customer sender, String receiverName, String destination, double weight) {
        super(trackingId, sender, receiverName, destination, weight);
    }

    @Override
    public double calculateCost() {
        return getWeight() * RATE_PER_KG;
    }

    @Override
    public String getParcelType() {
        return "Standard";
    }
}

// Express Parcel Subclass (Polymorphism: Higher base fee + fast rate)
class ExpressParcel extends Parcel {
    private static final double RATE_PER_KG = 250.0;
    private static final double EXPRESS_FEE = 500.0;

    public ExpressParcel(String trackingId, Customer sender, String receiverName, String destination, double weight) {
        super(trackingId, sender, receiverName, destination, weight);
    }

    @Override
    public double calculateCost() {
        return (getWeight() * RATE_PER_KG) + EXPRESS_FEE;
    }

    @Override
    public String getParcelType() {
        return "Express";
    }
}

// Fragile Parcel Subclass (Polymorphism: Adds insurance handling fee)
class FragileParcel extends Parcel {
    private static final double RATE_PER_KG = 200.0;
    private static final double HANDLING_FEE = 350.0;

    public FragileParcel(String trackingId, Customer sender, String receiverName, String destination, double weight) {
        super(trackingId, sender, receiverName, destination, weight);
    }

    @Override
    public double calculateCost() {
        return (getWeight() * RATE_PER_KG) + HANDLING_FEE;
    }

    @Override
    public String getParcelType() {
        return "Fragile";
    }
}

// ==========================================
// 4. MANAGEMENT CONTROLLER (CRUD OPERATIONS)
// ==========================================
class ParcelManager {
    private List<Parcel> parcelList = new ArrayList<>();
    private int trackingCounter = 1001;

    public String generateTrackingId() {
        return "TRK" + (trackingCounter++);
    }

    // CREATE
    public void addParcel(Parcel parcel) {
        parcelList.add(parcel);
    }

    // READ
    public List<Parcel> getAllParcels() {
        return parcelList;
    }

    public Parcel findParcel(String trackingId) {
        for (Parcel p : parcelList) {
            if (p.getTrackingId().equalsIgnoreCase(trackingId)) {
                return p;
            }
        }
        return null;
    }

    // UPDATE
    public boolean updateParcel(String trackingId, String receiver, String destination, double weight, String status) {
        Parcel p = findParcel(trackingId);
        if (p != null) {
            p.setReceiverName(receiver);
            p.setDestination(destination);
            p.setWeight(weight);
            p.setStatus(status);
            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteParcel(String trackingId) {
        Parcel p = findParcel(trackingId);
        if (p != null) {
            parcelList.remove(p);
            return true;
        }
        return false;
    }
}

// ==========================================
// 5. SWING USER INTERFACE (DESKTOP GUI)
// ==========================================
public class CourierManagementSystem extends JFrame {

    private ParcelManager manager = new ParcelManager();

    // Form Components
    private JTextField txtSenderName, txtSenderPhone, txtReceiverName, txtDestination, txtWeight, txtTrackingId;
    private JComboBox<String> cbParcelType, cbStatus;
    private JTable table;
    private DefaultTableModel tableModel;

    public CourierManagementSystem() {
        setTitle("NIBM Courier Management System (OOP CW1)");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel lblTitle = new JLabel("Courier Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Input Form Panel (Left)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Parcel Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTrackingId = new JTextField(12);
        txtTrackingId.setEditable(false);
        txtTrackingId.setBackground(new Color(230, 230, 230));

        txtSenderName = new JTextField(12);
        txtSenderPhone = new JTextField(12);
        txtReceiverName = new JTextField(12);
        txtDestination = new JTextField(12);
        txtWeight = new JTextField(12);

        cbParcelType = new JComboBox<>(new String[]{"Standard", "Express", "Fragile"});
        cbStatus = new JComboBox<>(new String[]{"Pending", "In Transit", "Delivered", "Cancelled"});

        int y = 0;
        addFormRow(formPanel, gbc, y++, "Tracking ID (Auto):", txtTrackingId);
        addFormRow(formPanel, gbc, y++, "Sender Name:", txtSenderName);
        addFormRow(formPanel, gbc, y++, "Sender Phone:", txtSenderPhone);
        addFormRow(formPanel, gbc, y++, "Receiver Name:", txtReceiverName);
        addFormRow(formPanel, gbc, y++, "Destination:", txtDestination);
        addFormRow(formPanel, gbc, y++, "Weight (kg):", txtWeight);
        addFormRow(formPanel, gbc, y++, "Parcel Type:", cbParcelType);
        addFormRow(formPanel, gbc, y++, "Status:", cbStatus);

        add(formPanel, BorderLayout.WEST);

        // Table Panel (Center)
        String[] columns = {"Tracking ID", "Sender", "Receiver", "Destination", "Type", "Weight", "Cost (LKR)", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Shipment Records"));
        add(scrollPane, BorderLayout.CENTER);

        // Table Selection Listener (Fills input fields when a row is clicked)
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                txtTrackingId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtSenderName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtReceiverName.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtDestination.setText(tableModel.getValueAt(selectedRow, 3).toString());
                cbParcelType.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
                txtWeight.setText(tableModel.getValueAt(selectedRow, 5).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
            }
        });

        // Button Panel (South) - CRUD Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnCreate = new JButton("Create Shipment");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnClear = new JButton("Clear Form");

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnCreate.addActionListener(this::handleCreate);
        btnUpdate.addActionListener(this::handleUpdate);
        btnDelete.addActionListener(this::handleDelete);
        btnClear.addActionListener(e -> clearForm());

        // Load Initial Sample Data
        seedSampleData();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    // CREATE ACTION
    private void handleCreate(ActionEvent e) {
        try {
            String sender = txtSenderName.getText().trim();
            String phone = txtSenderPhone.getText().trim();
            String receiver = txtReceiverName.getText().trim();
            String destination = txtDestination.getText().trim();
            double weight = Double.parseDouble(txtWeight.getText().trim());
            String type = (String) cbParcelType.getSelectedItem();

            if (sender.isEmpty() || receiver.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all text fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer cust = new Customer("C" + System.currentTimeMillis() % 1000, sender, phone, "Default Address");
            String trackingId = manager.generateTrackingId();

            Parcel parcel;
            switch (type) {
                case "Express":
                    parcel = new ExpressParcel(trackingId, cust, receiver, destination, weight);
                    break;
                case "Fragile":
                    parcel = new FragileParcel(trackingId, cust, receiver, destination, weight);
                    break;
                default:
                    parcel = new StandardParcel(trackingId, cust, receiver, destination, weight);
                    break;
            }

            manager.addParcel(parcel);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Shipment Created Successfully!\nTracking ID: " + trackingId, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for weight.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE ACTION
    private void handleUpdate(ActionEvent e) {
        String trackingId = txtTrackingId.getText().trim();
        if (trackingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a record from the table to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String receiver = txtReceiverName.getText().trim();
            String destination = txtDestination.getText().trim();
            double weight = Double.parseDouble(txtWeight.getText().trim());
            String status = (String) cbStatus.getSelectedItem();

            boolean success = manager.updateParcel(trackingId, receiver, destination, weight, status);
            if (success) {
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Shipment " + trackingId + " updated successfully.", "Update Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid weight format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE ACTION
    private void handleDelete(ActionEvent e) {
        String trackingId = txtTrackingId.getText().trim();
        if (trackingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a record from the table to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete shipment " + trackingId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteParcel(trackingId)) {
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Shipment deleted successfully.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Parcel p : manager.getAllParcels()) {
            Object[] row = {
                p.getTrackingId(),
                p.getSender().getName(),
                p.getReceiverName(),
                p.getDestination(),
                p.getParcelType(),
                p.getWeight(),
                String.format("%.2f", p.calculateCost()),
                p.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void clearForm() {
        txtTrackingId.setText("");
        txtSenderName.setText("");
        txtSenderPhone.setText("");
        txtReceiverName.setText("");
        txtDestination.setText("");
        txtWeight.setText("");
        cbParcelType.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private void seedSampleData() {
        Customer c1 = new Customer("C001", "Amal Perera", "0771234567", "Colombo");
        Customer c2 = new Customer("C002", "Kamal Silva", "0719876543", "Kandy");

        manager.addParcel(new StandardParcel(manager.generateTrackingId(), c1, "Nimal Siri", "Galle", 2.5));
        manager.addParcel(new ExpressParcel(manager.generateTrackingId(), c2, "Sunil Fernando", "Jaffna", 1.2));
        manager.addParcel(new FragileParcel(manager.generateTrackingId(), c1, "Kasun Raj", "Matara", 4.0));

        refreshTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourierManagementSystem().setVisible(true);
        });
    }
}