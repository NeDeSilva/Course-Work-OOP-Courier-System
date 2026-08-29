import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Customer workspace: create shipments and track their status. */
public class CustomerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultListModel<Shipment> shipmentModel = new DefaultListModel<>();
    private final JList<Shipment> shipmentList = new JList<>(shipmentModel);

    public CustomerPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        top.setOpaque(false);
        JTextField customerGovField = UITheme.field(14);
        JButton loadBtn = UITheme.primaryButton("Load Shipments");
        top.add(UITheme.label("Customer GovID:"));
        top.add(customerGovField);
        top.add(loadBtn);

        JPanel sellerBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        sellerBox.setOpaque(false);
        JComboBox<Person> sellerCombo = new JComboBox<>();
        sellerCombo.setFont(UITheme.FONT_FIELD);
        for (Person p : controller.getPeople()) if (p instanceof Seller) sellerCombo.addItem(p);
        sellerCombo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                                                                   int index, boolean isSelected, boolean f) {
                super.getListCellRendererComponent(list, value, index, isSelected, f);
                if (value instanceof Person p) setText(p.getName() + " (" + p.getGovID() + ")");
                return this;
            }
        });
        sellerBox.add(UITheme.label("Seller:"));
        sellerBox.add(sellerCombo);

        JTextField descField = UITheme.field(20);
        JButton createBtn = UITheme.successButton("Create Shipment");
        sellerBox.add(UITheme.label("Description:"));
        sellerBox.add(descField);
        sellerBox.add(createBtn);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.add(top);
        form.add(sellerBox);

        shipmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shipmentList.setFont(UITheme.FONT_SUB);
        JScrollPane scroll = new JScrollPane(shipmentList);

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> {
            String gov = customerGovField.getText().trim();
            shipmentModel.clear();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter customer GovID to load shipments",
                        "Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Shipment> list = controller.getShipmentsForCustomer(gov);
            for (Shipment s : list) shipmentModel.addElement(s);
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No shipments found for that customer.",
                        "Shipments", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        createBtn.addActionListener(e -> {
            String gov = customerGovField.getText().trim();
            if (gov.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your GovID first",
                        "Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Person seller = (Person) sellerCombo.getSelectedItem();
            String desc = descField.getText().trim();
            String id = "SH-" + System.currentTimeMillis();
            String tracking = "TRK" + (System.currentTimeMillis() % 100000);
            Shipment s = new Shipment(id, tracking, gov, seller == null ? "" : seller.getGovID(), desc);
            controller.addShipment(s);
            shipmentModel.addElement(s);
            JOptionPane.showMessageDialog(this, "Shipment created: " + s.getTrackingCode(),
                    "Created", JOptionPane.INFORMATION_MESSAGE);
            descField.setText("");
        });

        shipmentList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Shipment s = shipmentList.getSelectedValue();
                    if (s != null) {
                        JOptionPane.showMessageDialog(CustomerPanel.this,
                                "Tracking: " + s.getTrackingCode() + "\nStatus: " + s.getStatus()
                                        + "\nDriver: " + s.getDriverGovID(),
                                "Shipment detail", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }
}
