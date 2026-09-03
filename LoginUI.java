import javax.swing.*;
import java.awt.*;

/**
 * LoginUI - username + password form. Valid credentials open the matching
 * screen: Admin, Driver, or Sender. Reserver accounts go back to Home.
 */
public class LoginUI extends CoreUI {

	private JTextField userField;
	private JPasswordField passField;
	private JLabel status;

	public LoginUI() {
		buildBody();
		linkBack(new HomeUI());
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(360, 300));
		card.setMaximumSize(new Dimension(360, 300));

		card.add(centred(Stylier.h1("Log in")));
		card.add(Box.createVerticalStrut(16));

		card.add(centred(Stylier.h2("Username")));
		userField = Stylier.field();
		userField.setMaximumSize(new Dimension(300, 36));
		userField.addActionListener(e -> passField.requestFocusInWindow());
		card.add(userField);

		card.add(Box.createVerticalStrut(10));
		card.add(centred(Stylier.h2("Password")));
		passField = Stylier.passwordField();
		passField.setMaximumSize(new Dimension(300, 36));
		passField.addActionListener(e -> login());
		card.add(passField);

		card.add(Box.createVerticalStrut(14));
		JButton login = Stylier.button("Log In", true);
		login.addActionListener(e -> login());
		card.add(centred(login));

		status = Stylier.subtle(" ");
		status.setForeground(Theme.ERROR);
		card.add(Box.createVerticalStrut(8));
		card.add(centred(status));

		showCard(card);
	}

	private static JComponent centred(JComponent c) {
		c.setAlignmentX(Component.CENTER_ALIGNMENT);
		return c;
	}

	private void login() {
		String u = userField.getText().trim();
		String p = new String(passField.getPassword());
		if (u.isEmpty() || p.isEmpty()) {
			status.setText("Enter both username and password.");
			return;
		}
		try {
			if (new AdminDAO().isValid(u, p)) {
				goTo(new AdminUI(u));
			} else if (new DriverDAO().isValid(u, p)) {
				goTo(new DriverUI(u));
			} else if (new SenderDAO().isValid(u, p)) {
				goTo(new SenderUI(new SenderDAO().findByCredentials(u, p)));
			} else if (new ReserverDAO().isValid(u, p)) {
				goTo(new HomeUI());
			} else {
				status.setText("Invalid credentials. Try again.");
			}
		} catch (Exception ex) {
			status.setText("Login error: " + ex.getMessage());
		}
	}
}
