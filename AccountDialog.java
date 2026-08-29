import javax.swing.*;
import java.awt.*;

/**
 * Modal dialog for collecting a new account's details and building the
 * matching {@link Person} subtype (Customer, Seller or Driver).
 */
public class AccountDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Class<? extends Person> type;
    private Person result;

    private final JTextField govId = new JTextField(14);
    private final JTextField name = new JTextField(14);
    private final JTextField age = new JTextField(14);
    private final JTextField address = new JTextField(14);
    private final JTextField phone = new JTextField(14);
    private final JTextField email = new JTextField(14);
    private final JTextField username = new JTextField(14);
    private final JPasswordField password = new JPasswordField(14);
    private final JTextField extra1 = new JTextField(14); // shop / license
    private final JTextField extra2 = new JTextField(14); // (vehicle for drivers)

    public AccountDialog(Window owner, Class<? extends Person> type) {
        super(owner, "Add " + type.getSimpleName(), ModalityType.APPLICATION_MODAL);
        this.type = type;
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout(8, 8));

        JPanel body = UITheme.card();
        body.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        body.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        String extra1Label = type == Seller.class ? "Shop name" : "License number";
        String extra2Label = type == Driver.class ? "Vehicle number" : null;

        addRow(body, c, 0, "Gov ID", govId);
        addRow(body, c, 1, "Name", name);
        addRow(body, c, 2, "Age", age);
        addRow(body, c, 3, "Address", address);
        addRow(body, c, 4, "Phone", phone);
        addRow(body, c, 5, "Email", email);
        addRow(body, c, 6, "Username", username);
        addRow(body, c, 7, "Password", password);
        addRow(body, c, 8, extra1Label, extra1);
        if (extra2Label != null) {
            addRow(body, c, 9, extra2Label, extra2);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton ok = UITheme.primaryButton("Create");
        JButton cancel = UITheme.secondaryButton("Cancel");
        ok.addActionListener(e -> { if (buildPerson()) dispose(); });
        cancel.addActionListener(e -> dispose());
        buttons.add(cancel);
        buttons.add(ok);

        add(body, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel panel, GridBagConstraints c, int y, String labelText, java.awt.Component field) {
        JLabel lbl = UITheme.label(labelText);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridy = y; c.gridx = 0; c.weightx = 0.2;
        panel.add(lbl, c);
        c.gridx = 1; c.weightx = 1;
        panel.add(field, c);
    }

    private boolean buildPerson() {
        String id = govId.getText().trim();
        String nm = name.getText().trim();
        String u = username.getText().trim();
        String p = new String(password.getPassword());
        if (id.isEmpty() || nm.isEmpty() || u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Gov ID, Name, Username and Password are required.",
                    "Input error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int a;
        try {
            a = Integer.parseInt(age.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Input error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String addr = address.getText().trim();
        String ph = phone.getText().trim();
        String em = email.getText().trim();

        if (type == Seller.class) {
            result = new Seller(id, nm, a, addr, ph, em, u, p, extra1.getText().trim());
        } else if (type == Driver.class) {
            result = new Driver(id, nm, a, addr, ph, em, u, p, extra1.getText().trim(), extra2.getText().trim());
        } else {
            result = new Customer(id, nm, a, addr, ph, em, u, p);
        }
        return true;
    }

    public Person getResult() {
        return result;
    }
}
