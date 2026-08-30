import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * DriverUI - lets a driver pick an active parcel and update its status.
 */
public class DriverUI extends CoreUI {

	private JComboBox<String> parcelBox;
	private JComboBox<String> statusBox;
	private JLabel sessionLabel;
	private JLabel result;
	private List<Parcel> parcels;

	public DriverUI(String username) {
		buildBody();
		linkBack(new LoginUI());
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(520, 320));
		card.setMaximumSize(new Dimension(520, 360));

		card.add(Stylier.h1("Driver Console"));
		card.add(Box.createVerticalStrut(14));

		card.add(Stylier.h2("Select parcel"));
		parcelBox = new JComboBox<>();
		parcelBox.setPreferredSize(new Dimension(420, 32));
		parcelBox.addActionListener(e -> showDetails());
		card.add(parcelBox);

		card.add(Box.createVerticalStrut(14));
		sessionLabel = Stylier.body("Status: -");
		card.add(sessionLabel);

		card.add(Box.createVerticalStrut(14));
		card.add(Stylier.h2("Set new status"));
		statusBox = new JComboBox<>(new SessionDAO().distinctStatuses().toArray(new String[0]));
		statusBox.setPreferredSize(new Dimension(260, 32));
		card.add(statusBox);

		card.add(Box.createVerticalStrut(14));
		JButton update = Stylier.button("Update Status", true);
		update.addActionListener(e -> updateStatus());
		card.add(update);

		result = Stylier.subtle(" ");
		card.add(Box.createVerticalStrut(8));
		card.add(result);

		showCard(card);
		loadParcels();
	}

	private void loadParcels() {
		try {
			parcels = new ParcelDAO().listAllActive();
			parcelBox.removeAllItems();
			if (parcels.isEmpty()) {
				parcelBox.addItem("No active parcels");
			} else {
				for (Parcel p : parcels) {
					parcelBox.addItem(p.getTrackingNumber() + "  -  " + p.getName());
				}
				showDetails();
			}
		} catch (Exception ex) {
			parcelBox.addItem("Failed to load");
		}
	}

	private void showDetails() {
		if (parcels == null || parcels.isEmpty() || parcelBox.getSelectedIndex() < 0) {
			return;
		}
		Parcel p = parcels.get(parcelBox.getSelectedIndex());
		try {
			Session s = new SessionDAO().findByTracking(p.getTrackingNumber());
			String status = s == null ? "-" : s.getStatus();
			sessionLabel.setText("Status: " + status + "   (" + p.getName() + ")");
		} catch (Exception ex) {
			sessionLabel.setText("Status: -");
		}
	}

	private void updateStatus() {
		if (parcels == null || parcels.isEmpty() || parcelBox.getSelectedIndex() < 0) {
			return;
		}
		Parcel p = parcels.get(parcelBox.getSelectedIndex());
		String status = (String) statusBox.getSelectedItem();
		try {
			new SessionDAO().updateStatus(p.getTrackingNumber(), status);
			result.setText("Updated '" + p.getTrackingNumber() + "' to '" + status + "'");
			showDetails();
		} catch (Exception ex) {
			result.setText("Error: " + ex.getMessage());
		}
	}
}
