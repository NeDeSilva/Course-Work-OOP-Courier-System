import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * AdminUI - dashboard for an administrator: shows active parcels in a table.
 */
public class AdminUI extends CoreUI {

	private JTable table;

	public AdminUI(String username) {
		buildBody();
		linkBack(new LoginUI());
	}

	@Override
	protected void buildBody() {
		JPanel card = Stylier.card();
		card.setLayout(new BorderLayout(0, 12));
		card.setPreferredSize(new Dimension(640, 420));

		JPanel head = new JPanel(new BorderLayout());
		head.setOpaque(false);
		head.add(Stylier.h1("Admin Dashboard"), BorderLayout.WEST);
		card.add(head, BorderLayout.NORTH);

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setRowHeight(28);
		table.getTableHeader().setBackground(Theme.YELLOW_SOFT);
		card.add(new JScrollPane(table), BorderLayout.CENTER);

		showCard(card);
		load();
	}

	private void load() {
		try {
			List<Parcel> parcels = new ParcelDAO().listAllActive();
			SessionDAO sd = new SessionDAO();
			DriverDAO dd = new DriverDAO();

			String[] cols = { "Tracking", "Parcel", "Status", "Driver", "From", "To" };
			javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}
			};

			for (Parcel p : parcels) {
				Session s = sd.findByTracking(p.getTrackingNumber());
				String status = s == null ? "-" : s.getStatus();
				String driver = s == null || s.getDriverId() <= 0
						? "Unassigned" : dd.nameOf(s.getDriverId());
				model.addRow(new Object[] {
						p.getTrackingNumber(),
						p.getName(),
						status,
						driver,
						p.getSenderAddress(),
						p.getReceiverAddress()
				});
			}
			table.setModel(model);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
