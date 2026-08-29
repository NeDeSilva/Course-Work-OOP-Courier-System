import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Driver workspace: load assigned shipments and advance their status. */
public class DriverPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultListModel<Shipment> shipmentModel = new DefaultListModel<>();
    private final JList<Shipment> shipmentList = new JList<>(shipmentModel);

    public DriverPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        top.setOpaque(false);
        JTextField driverGovField = UITheme.field(14);
        JButton loadBtn = UITheme.primaryButton("Load Assigned");
        JButton pickBtn = UITheme.secondaryButton("Mark Picked");
        JButton deliverBtn = UITheme.successButton("Mark Delivered");
        top.add(UITheme.label("Driver GovID:"));
        top.add(driverGovField);
        top.add(loadBtn);
        top.add(pickBtn);
        top.add(deliverBtn);

        shipmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shipmentList.setFont(UITheme.FONT_SUB);
        JScrollPane scroll = new JScrollPane(shipmentList);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> {
            String gov = driverGovField.getText().trim();
            shipmentModel.clear();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter driver GovID to load shipments",
                        "Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Shipment> list = controller.getShipmentsForDriver(gov);
            for (Shipment s : list) shipmentModel.addElement(s);
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No assigned shipments found.",
                        "Shipments", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pickBtn.addActionListener(e -> setStatus("Picked up"));
        deliverBtn.addActionListener(e -> setStatus("Delivered"));

        shipmentList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Shipment s = shipmentList.getSelectedValue();
                    if (s != null) {
                        JOptionPane.showMessageDialog(DriverPanel.this,
                                "Tracking: " + s.getTrackingCode() + "\nStatus: " + s.getStatus()
                                        + "\nDescription: " + s.getDescription(),
                                "Shipment detail", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    private void setStatus(String next) {
        Shipment s = shipmentList.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Select a shipment");
            return;
        }
        controller.updateShipmentStatus(s.getId(), next);
        shipmentList.repaint();
    }
}
