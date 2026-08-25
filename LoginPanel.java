import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    public interface LoginListener { void onLogin(Person user); }

    private final AppController controller;

    public LoginPanel(AppController controller, LoginListener listener) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Courier System Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(title, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0;
        add(new JLabel("Username:"), c);
        JTextField usernameField = new JTextField(14);
        c.gridx = 1; add(usernameField, c);

        c.gridy = 2; c.gridx = 0; add(new JLabel("Password:"), c);
        JPasswordField passwordField = new JPasswordField(14);
        c.gridx = 1; add(passwordField, c);

        c.gridy = 3; c.gridx = 0; add(new JLabel("Role:"), c);
        JComboBox<String> roleCombo = new JComboBox<>(new String[] {"Customer","Seller","Driver","Admin"});
        c.gridx = 1; add(roleCombo, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(loginBtn); buttons.add(cancelBtn);
        c.gridy = 4; c.gridx = 0; c.gridwidth = 2; add(buttons, c);

        loginBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = ((String) roleCombo.getSelectedItem()).toLowerCase();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Enter username and password", "Login", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Person matched = null;
                for (Person p : controller.getPeople()) {
                    if (p.userName.equals(username) && p.password.equals(password)) {
                        matched = p;
                        break;
                    }
                }
                if (matched == null) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password.", "Login failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // If the selected role doesn't match the concrete user type, warn but allow login
                String actualRole = matched.getClass().getSimpleName().toLowerCase();
                if (!actualRole.equals(role)) {
                    int resp = JOptionPane.showConfirmDialog(LoginPanel.this,
                            "You selected role '" + role + "' but the account is a '" + actualRole + "'. Continue?",
                            "Role mismatch", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (resp != JOptionPane.YES_OPTION) return;
                }
                listener.onLogin(matched);
            }
        });

        cancelBtn.addActionListener(a -> {
            Window w = SwingUtilities.getWindowAncestor(LoginPanel.this);
            if (w != null) w.dispose();
        });
    }
}
