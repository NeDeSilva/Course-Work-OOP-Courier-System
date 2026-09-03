import javax.swing.*;
import java.awt.*;

/**
 * SenderUI - lets a logged-in sender send a new parcel and get a tracking id.
 */
public class SenderUI extends CoreUI {

	private final Sender sender;
	private JTextField nameField;
	private JTextField weightField;
	private JTextField receiverAddrField;
	private JLabel status;

	public SenderUI(Sender sender) {
		this.sender = sender;
		buildBody();
		linkBack(new LoginUI());
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setPreferredSize(new Dimension(480, 340));
		card.setMaximumSize(new Dimension(480, 360));

		card.add(Stylier.h1("Send a Parcel"));
		card.add(Box.createVerticalStrut(16));

		card.add(Stylier.h2("Parcel Name"));
		nameField = Stylier.field();
		card.add(nameField);

		card.add(Box.createVerticalStrut(10));
		card.add(Stylier.h2("Weight (kg)"));
		weightField = Stylier.field();
		card.add(weightField);

		card.add(Box.createVerticalStrut(10));
		card.add(Stylier.h2("Receiver Address"));
		receiverAddrField = Stylier.field();
		card.add(receiverAddrField);

		card.add(Box.createVerticalStrut(14));
		JButton send = Stylier.button("Send Parcel", true);
		send.addActionListener(e -> sendParcel());
		card.add(send);

		status = Stylier.subtle(" ");
		card.add(Box.createVerticalStrut(8));
		card.add(status);

		showCard(card);
	}

	private void sendParcel() {
		String name = nameField.getText().trim();
		if (name.isEmpty()) {
			status.setText("Please fill the parcel name.");
			return;
		}
		double weight;
		try {
			weight = weightField.getText().trim().isEmpty()
					? 1.0 : Double.parseDouble(weightField.getText().trim());
		} catch (NumberFormatException ex) {
			status.setText("Weight must be a number.");
			return;
		}
		try {
			Parcel p = new Parcel();
			p.setName(name);
			p.setWeight(weight);
			p.setReceiverAddress(receiverAddrField.getText().trim());
			p.setSenderId(sender.getId());
			String tracking = new ParcelDAO().add(p, "Registered");
			status.setText("Parcel sent! Tracking ID: " + tracking);
		} catch (Exception ex) {
			status.setText("Error: " + ex.getMessage());
		}
	}
}
