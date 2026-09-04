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
		// Parent container using GridBagLayout to center the card on the whole page
		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);

		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

		// Internal padding around the card edges
		card.setBorder(BorderFactory.createEmptyBorder(30, 32, 30, 32));

		// Dimension adjustments for a cleaner, well-proportioned layout
		card.setPreferredSize(new Dimension(420, 380));
		card.setMaximumSize(new Dimension(420, 380));

		card.add(centred(Stylier.h1("Courier Management")));
		card.add(Box.createVerticalStrut(6));
		card.add(centred(Stylier.subtle("Track your parcel")));

		card.add(Box.createVerticalStrut(24));
		card.add(centred(Stylier.h2("Tracking ID")));
		card.add(Box.createVerticalStrut(10));

		trackingField = Stylier.field();
		trackingField.setMaximumSize(new Dimension(340, 38));
		trackingField.setAlignmentX(Component.CENTER_ALIGNMENT);
		trackingField.addActionListener(e -> track());
		card.add(trackingField);

		card.add(Box.createVerticalStrut(16));

		JButton track = Stylier.button("Track Parcel", true);
		track.setAlignmentX(Component.CENTER_ALIGNMENT);
		track.addActionListener(e -> track());
		card.add(track);

		status = Stylier.subtle(" ");
		status.setForeground(Theme.ERROR);
		card.add(Box.createVerticalStrut(10));
		card.add(centred(status));

		JButton login = Stylier.linkButton("Log in here");
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		login.addActionListener(e -> goTo(new LoginUI()));
		card.add(Box.createVerticalStrut(10));
		card.add(login);

		// Place card inside wrapper, then pass wrapper to showCard
		wrapper.add(card);
		showCard(wrapper);
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
