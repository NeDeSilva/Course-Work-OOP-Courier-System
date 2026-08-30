import javax.swing.*;
import java.awt.*;

/**
 * HomeUI - landing screen. Enter a tracking id to view a parcel, or go to
 * the login screen.
 */
public class HomeUI extends CoreUI {

	private JTextField trackingField;
	private JLabel status;

	public HomeUI() {
		buildBody();
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(400, 300));
		card.setMaximumSize(new Dimension(400, 300));

		card.add(centred(Stylier.h1("Courier Management")));
		card.add(centred(Stylier.subtle("Track your parcel")));
		card.add(Box.createVerticalStrut(20));
		card.add(centred(Stylier.h2("Tracking ID")));

		trackingField = Stylier.field();
		trackingField.setMaximumSize(new Dimension(320, 36));
		trackingField.addActionListener(e -> track());
		card.add(trackingField);

		card.add(Box.createVerticalStrut(12));

		JButton track = Stylier.button("Track Parcel", true);
		track.addActionListener(e -> track());
		card.add(centred(track));

		status = Stylier.subtle(" ");
		status.setForeground(Theme.ERROR);
		card.add(Box.createVerticalStrut(8));
		card.add(centred(status));

		JButton login = Stylier.linkButton("Log in here");
		login.addActionListener(e -> goTo(new LoginUI()));
		card.add(Box.createVerticalStrut(8));
		card.add(centred(login));

		showCard(card);
	}

	private static JComponent centred(JComponent c) {
		c.setAlignmentX(Component.CENTER_ALIGNMENT);
		return c;
	}

	private void track() {
		String id = trackingField.getText().trim();
		if (id.isEmpty()) {
			status.setText("Please enter a tracking ID.");
			return;
		}
		try {
			Parcel p = new ParcelDAO().findByTracking(id);
			if (p == null) {
				status.setText("No parcel found for that tracking ID.");
			} else {
				goTo(new ReserverUI(p));
			}
		} catch (Exception ex) {
			status.setText("Error: " + ex.getMessage());
		}
	}
}
