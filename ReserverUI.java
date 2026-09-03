import javax.swing.*;
import java.awt.*;

/**
 * ReserverUI - shows the current state and details of a traced parcel.
 */
public class ReserverUI extends CoreUI {

	private final Parcel parcel;

	public ReserverUI(Parcel parcel) {
		this.parcel = parcel;
		buildBody();
		linkBack(new HomeUI());
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(420, 340));
		card.setMaximumSize(new Dimension(420, 360));

		card.add(Stylier.h1("Parcel Details"));
		card.add(Box.createVerticalStrut(6));
		card.add(Stylier.subtle("Tracking ID:  " + parcel.getTrackingNumber()));
		card.add(Box.createVerticalStrut(14));

		addDetail(card, "Parcel", parcel.getName());
		addDetail(card, "Weight", parcel.getWeight() + " kg");
		addDetail(card, "Size", parcel.getSize() + " cm");
		addDetail(card, "Sender address", parcel.getSenderAddress());
		addDetail(card, "Receiver address", parcel.getReceiverAddress());

		Session session = loadSession();
		String status = session == null ? "Unknown" : session.getStatus();
		addDetail(card, "Status", status);
		if (session != null && !session.getDeliveryDate().isEmpty()) {
			addDetail(card, "Delivery by", session.getDeliveryDate());
		}

		card.add(Box.createVerticalStrut(14));
		JButton again = Stylier.button("Track Another", false);
		again.addActionListener(e -> goTo(new HomeUI()));
		card.add(again);

		showCard(card);
	}

	private Session loadSession() {
		try {
			return new SessionDAO().findByTracking(parcel.getTrackingNumber());
		} catch (Exception ex) {
			return null;
		}
	}

	private void addDetail(JPanel card, String key, String value) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		row.setOpaque(false);
		JLabel k = Stylier.h2(key);
		k.setForeground(Theme.MUTED);
		row.add(k);
		row.add(Stylier.body(value == null || value.isEmpty() ? "-" : value));
		card.add(row);
		card.add(Box.createVerticalStrut(8));
	}
}
