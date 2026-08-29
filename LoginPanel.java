import javax.swing.*;
import java.awt.*;

/** Modern login view. Delegates credential checking to the controller. */
public class LoginPanel extends JPanel {

    public interface LoginListener {
        void onLogin(Person user);
    }

    private final JTextField usernameField = new JTextField(16);
    private final JPasswordField passwordField = new JPasswordField(16);
    private final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Customer", "Seller", "Driver", "Admin"});
    private final LoginListener listener;

    public LoginPanel(AppController controller, LoginListener listener) {
        this.listener = listener;
        setBackground(UITheme.BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setBorder(BorderFactory.createEmptyBorder(34, 38, 34, 38));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 6, 8, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        JLabel title = UITheme.header("Courier System Login");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);

        JLabel subtitle = UITheme.label("Sign in with your account to continue");
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1;
        card.add(subtitle, c);

        addCardRow(card, c, 2, "Username", usernameField);
        addCardRow(card, c, 3, "Password", passwordField);

        JLabel roleLbl = UITheme.label("Role");
        roleLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridy = 4; c.gridwidth = 1; c.gridx = 0;
        card.add(roleLbl, c);
        roleCombo.setFont(UITheme.FONT_FIELD);
        c.gridx = 1;
        card.add(roleCombo, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        JButton loginBtn = UITheme.primaryButton("Login");
        JButton cancelBtn = UITheme.secondaryButton("Cancel");
        buttons.add(cancelBtn);
        buttons.add(loginBtn);
        c.gridy = 5; c.gridx = 0; c.gridwidth = 2;
        card.add(buttons, c);

        GridBagConstraints outer = new GridBagConstraints();
        outer.insets = new Insets(20, 20, 20, 20);
        add(card, outer);

        loginBtn.addActionListener(e -> tryLogin(controller));
        usernameField.addActionListener(e -> tryLogin(controller));
        passwordField.addActionListener(e -> tryLogin(controller));
        cancelBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(LoginPanel.this);
            if (w != null) w.dispose();
        });
    }

    private void addCardRow(JPanel card, GridBagConstraints c, int y, String labelText, JComponent field) {
        JLabel lbl = UITheme.label(labelText);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridy = y; c.gridwidth = 1; c.gridx = 0;
        card.add(lbl, c);
        c.gridx = 1;
        card.add(field, c);
    }

    private void tryLogin(AppController controller) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = ((String) roleCombo.getSelectedItem()).toLowerCase();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password", "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Person matched = controller.authenticate(username, password);
        if (matched == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String actualRole = matched.getClass().getSimpleName().toLowerCase();
        if (!actualRole.equals(role)) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "You selected role '" + role + "' but the account is a '" + actualRole + "'. Continue?",
                    "Role mismatch", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (resp != JOptionPane.YES_OPTION) return;
        }
        listener.onLogin(matched);
    }
}
