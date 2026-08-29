import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Admin view of all shipments: shows a live table, allows assigning a
 * selected shipment to a driver and advancing its status, and creating
 * new shipments from the admin side.
 */
public class ShipmentsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel status = new JLabel(" ");

    public ShipmentsPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        model = new DefaultTableModel(
                new String[]{"ID", "Tracking", "Customer", "Seller", "Driver", "Status", "Description"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setOpaque(false);
        JButton refresh = UITheme.primaryButton("Refresh");
        refresh.addActionListener(e -> refreshTable());
        JButton assign = UITheme.successButton("Assign to Driver");
        assign.addActionListener(e -> assignDriver());
        JButton advance = UITheme.secondaryButton("Advance Status");
        advance.addActionListener(e -> advanceStatus());
        top.add(refresh);
        top.add(assign);
        top.add(advance);

        status.setForeground(UITheme.TEXT_MUTED);
        status.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 6));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        refreshTable();
    }

    private Shipment selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            setStatus("Select a shipment first.");
            return null;
        }
        String id = (String) model.getValueAt(row, 0);
        return controller.getShipments().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst().orElse(null);
    }

    private void assignDriver() {
        Shipment s = selected();
        if (s == null) return;
        java.util.List<Driver> drivers = new java.util.ArrayList<>();
        for (Person p : controller.getPeople()) if (p instanceof Driver) drivers.add((Driver) p);
        if (drivers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No drivers available. Add a driver in the Users tab first.",
                    "No drivers", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Driver chosen = (Driver) JOptionPane.showInputDialog(this,
                "Select a driver:", "Assign driver",
                JOptionPane.PLAIN_MESSAGE, null,
                drivers.toArray(), drivers.get(0));
        if (chosen == null) return;
        controller.assignDriverToShipment(s.getId(), chosen.getGovID());
        refreshTable();
        setStatus("Assigned " + chosen.getName() + " to " + s.getTrackingCode());
    }

    private void advanceStatus() {
        Shipment s = selected();
        if (s == null) return;
        String next = nextStatus(s.getStatus());
        controller.updateShipmentStatus(s.getId(), next);
        refreshTable();
        setStatus(s.getTrackingCode() + " → " + next);
    }

    private static String nextStatus(String current) {
        switch (current == null ? "" : current) {
            case "Created": return "Picked up";
            case "Picked up": return "In transit";
            case "In transit": return "Delivered";
            case "Assigned": return "Delivered";
            case "": return "Created";
            default: return "Delivered";
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Shipment s : controller.getShipments()) {
            model.addRow(new Object[]{
                    s.getId(), s.getTrackingCode(), label(s.getCustomerGovID()),
                    label(s.getSellerGovID()), label(s.getDriverGovID()), s.getStatus(), s.getDescription()});
        }
        status.setText("Total shipments: " + controller.getShipments().size());
    }

    private String label(String govID) {
        if (govID == null || govID.isEmpty()) return "—";
        for (Person p : controller.getPeople()) {
            if (govID.equals(p.getGovID())) return p.getName();
        }
        return govID;
    }

    private void setStatus(String message) {
        status.setForeground(UITheme.PRIMARY);
        status.setText(message);
    }
}
