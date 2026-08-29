import javax.swing.*;
import java.awt.*;

/** User management: list customers/sellers/drivers and add new accounts. */
public class UsersPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultListModel<Person> customerModel = new DefaultListModel<>();
    private final DefaultListModel<Person> sellerModel = new DefaultListModel<>();
    private final DefaultListModel<Person> driverModel = new DefaultListModel<>();

    private final JList<Person> customerList = new JList<>(customerModel);
    private final JList<Person> sellerList = new JList<>(sellerModel);
    private final JList<Person> driverList = new JList<>(driverModel);

    public UsersPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        styleList(customerList);
        styleList(sellerList);
        styleList(driverList);
        listener(customerList, Customer.class);
        listener(sellerList, Seller.class);
        listener(driverList, Driver.class);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customers", new JScrollPane(customerList));
        tabs.addTab("Sellers", new JScrollPane(sellerList));
        tabs.addTab("Drivers", new JScrollPane(driverList));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        top.setOpaque(false);
        JButton addCustomer = UITheme.primaryButton("Add Customer");
        JButton addSeller = UITheme.primaryButton("Add Seller");
        JButton addDriver = UITheme.primaryButton("Add Driver");
        JButton saveBtn = UITheme.secondaryButton("Save");
        addCustomer.addActionListener(e -> addUser(Customer.class));
        addSeller.addActionListener(e -> addUser(Seller.class));
        addDriver.addActionListener(e -> addUser(Driver.class));
        saveBtn.addActionListener(e -> saveAll());
        top.add(addCustomer);
        top.add(addSeller);
        top.add(addDriver);
        top.add(saveBtn);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refreshLists();
    }

    private void styleList(JList<Person> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(UITheme.FONT_SUB);
        list.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> l, Object value,
                                                                   int index, boolean isSelected, boolean f) {
                super.getListCellRendererComponent(l, value, index, isSelected, f);
                if (value instanceof Person p) {
                    setText(p.getName() + "   (" + p.getGovID() + ")");
                }
                return this;
            }
        });
    }

    private void listener(JList<Person> list, Class<? extends Person> type) {
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Person p = list.getSelectedValue();
                    if (p instanceof Customer) CustomerUI.show((Customer) p);
                    else if (p instanceof Seller) SellerUI.show((Seller) p);
                    else if (p instanceof Driver) DriverUI.show((Driver) p);
                }
            }
        });
    }

    private void addUser(Class<? extends Person> type) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        AccountDialog dialog = new AccountDialog(owner, type);
        dialog.setVisible(true);
        Person created = dialog.getResult();
        if (created != null) {
            controller.addUser(created);
            refreshLists();
        }
    }

    private void saveAll() {
        boolean ok = controller.saveAll();
        JOptionPane.showMessageDialog(this, ok ? "Saved." : "Save failed.",
                "Save", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private void refreshLists() {
        customerModel.clear();
        sellerModel.clear();
        driverModel.clear();
        for (Person p : controller.getPeople()) {
            if (p instanceof Seller) sellerModel.addElement(p);
            else if (p instanceof Driver) driverModel.addElement(p);
            else if (p instanceof Customer) customerModel.addElement(p);
        }
    }
}
