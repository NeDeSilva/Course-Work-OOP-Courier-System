import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DriverPanel extends JPanel {
    private final AppController controller;
    private final DefaultListModel<Shipment> shipmentModel = new DefaultListModel<>();
    private final JList<Shipment> shipmentList = new JList<>(shipmentModel);

    public DriverPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField driverGovField = new JTextField(12);
        JButton loadBtn = new JButton("Load Assigned");
        JButton pickBtn = new JButton("Mark Picked");
        JButton deliverBtn = new JButton("Mark Delivered");
        top.add(new JLabel("Driver GovID:")); top.add(driverGovField); top.add(loadBtn); top.add(pickBtn); top.add(deliverBtn);

        shipmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(shipmentList);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> {
            String gov = driverGovField.getText().trim();
            shipmentModel.clear();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter driver GovID to load shipments", "Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Shipment> list = controller.getShipmentsForDriver(gov);
            for (Shipment s : list) shipmentModel.addElement(s);
        });

        pickBtn.addActionListener(e -> {
            Shipment s = shipmentList.getSelectedValue();
            if (s == null) { JOptionPane.showMessageDialog(this, "Select a shipment"); return; }
            controller.updateShipmentStatus(s.getId(), "Picked up");
            shipmentList.repaint();
        });

        deliverBtn.addActionListener(e -> {
            Shipment s = shipmentList.getSelectedValue();
            if (s == null) { JOptionPane.showMessageDialog(this, "Select a shipment"); return; }
            controller.updateShipmentStatus(s.getId(), "Delivered");
            shipmentList.repaint();
        });

        shipmentList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Shipment s = shipmentList.getSelectedValue();
                    if (s != null) JOptionPane.showMessageDialog(DriverPanel.this, "Tracking: " + s.getTrackingCode() + "\nStatus: " + s.getStatus(), "Shipment detail", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
