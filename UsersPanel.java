import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    private final AppController controller;
    private final DefaultListModel<Person> customerModel = new DefaultListModel<>();
    private final DefaultListModel<Person> sellerModel = new DefaultListModel<>();
    private final DefaultListModel<Person> driverModel = new DefaultListModel<>();

    private final JList<Person> customerList = new JList<>(customerModel);
    private final JList<Person> sellerList = new JList<>(sellerModel);
    private final JList<Person> driverList = new JList<>(driverModel);

    // Double-click support: open detail UI for the selected user
    {
        customerList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = customerList.getSelectedValue();
                    if (p instanceof Customer) CustomerUI.show((Customer) p);
                }
            }
        });
        sellerList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = sellerList.getSelectedValue();
                    if (p instanceof Seller) SellerUI.show((Seller) p);
                }
            }
        });
        driverList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = driverList.getSelectedValue();
                    if (p instanceof Driver) DriverUI.show((Driver) p);
                }
            }
        });
    }

    public UsersPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8,8));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customers", new JScrollPane(customerList));
        tabs.addTab("Sellers", new JScrollPane(sellerList));
        tabs.addTab("Drivers", new JScrollPane(driverList));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addCustomer = new JButton("Add Customer");
        JButton addSeller = new JButton("Add Seller");
        JButton addDriver = new JButton("Add Driver");
        JButton saveBtn = new JButton("Save");
        top.add(addCustomer); top.add(addSeller); top.add(addDriver); top.add(saveBtn);

        // Global save shortcut (Ctrl+S)
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("control S"), "save");
        this.getActionMap().put("save", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                saveBtn.doClick();
            }
        });

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        addCustomer.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Customer name:");
            if (name != null) {
                Customer c = new Customer("C-" + System.currentTimeMillis(), name, 30, "", "", "", name.toLowerCase(), "pwd");
                controller.addUser(c);
                refreshLists();
            }
        });

        addSeller.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Seller name:");
            if (name != null) {
                Seller s = new Seller("S-" + System.currentTimeMillis(), name, 30, "", "", "", name.toLowerCase(), "pwd", "My Shop");
                controller.addUser(s);
                refreshLists();
            }
        });

        addDriver.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Driver name:");
            if (name != null) {
                Driver d = new Driver("D-" + System.currentTimeMillis(), name, 30, "", "", "", name.toLowerCase(), "pwd", "LIC-123", "VEH-1");
                controller.addUser(d);
                refreshLists();
            }
        });

        saveBtn.addActionListener(e -> {
            boolean ok = controller.saveAll();
            JOptionPane.showMessageDialog(this, ok ? "Saved" : "Save failed");
        });

        refreshLists();
    }

    private void refreshLists() {
        customerModel.clear(); sellerModel.clear(); driverModel.clear();
        for (Person p : controller.getPeople()) {
            if (p instanceof Seller) sellerModel.addElement(p);
            else if (p instanceof Driver) driverModel.addElement(p);
            else if (p instanceof Customer) customerModel.addElement(p);
        }
    }
}