import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final AppController controller;
    private final DefaultListModel<Shipment> shipmentModel = new DefaultListModel<>();
    private final JList<Shipment> shipmentList = new JList<>(shipmentModel);

    public CustomerPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField customerGovField = new JTextField(12);
        JButton loadBtn = new JButton("Load Shipments");
        top.add(new JLabel("Customer GovID:")); top.add(customerGovField); top.add(loadBtn);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField descField = new JTextField(20);
        JComboBox<Person> sellerCombo = new JComboBox<>();
        for (Person p : controller.getPeople()) if (p instanceof Seller) sellerCombo.addItem(p);
        JButton createBtn = new JButton("Create Shipment");
        form.add(new JLabel("Sellr:")); form.add(sellerCombo); form.add(new JLabel("Description:")); form.add(descField); form.add(createBtn);

        shipmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(shipmentList);

        add(top, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            String gov = customerGovField.getText().trim();
            shipmentModel.clear();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter customer GovID to load shipments", "Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Shipment> list = controller.getShipmentsForCustomer(gov);
            for (Shipment s : list) shipmentModel.addElement(s);
        });

        createBtn.addActionListener(e -> {
            String gov = customerGovField.getText().trim();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your GovID first", "Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Person seller = (Person) sellerCombo.getSelectedItem();
            String desc = descField.getText().trim();
            String id = "SH-" + System.currentTimeMillis();
            String tracking = "TRK" + (System.currentTimeMillis() % 100000);
            Shipment s = new Shipment(id, tracking, gov, seller == null ? "" : seller.getGovID(), desc);
            controller.addShipment(s);
            shipmentModel.addElement(s);
            JOptionPane.showMessageDialog(this, "Shipment created: " + s.getTrackingCode(), "Created", JOptionPane.INFORMATION_MESSAGE);
            descField.setText("");
        });

        shipmentList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Shipment s = shipmentList.getSelectedValue();
                    if (s != null) {
                        JOptionPane.showMessageDialog(CustomerPanel.this, "Tracking: " + s.getTrackingCode() + "\nStatus: " + s.getStatus() + "\nDriver: " + s.getDriverGovID(), "Shipment detail", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }
}
