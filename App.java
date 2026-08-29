import java.awt.*;
import javax.swing.*;

/**
 * App
 */
class App {

	public static int WHeight = 700;
	public static int WWidth = 1200;
	public static Color CPcolor = new Color(244, 241, 222);
	public static Color SPcolor = new Color(224, 122, 95);
	public static Dimension SPDimension = new Dimension(200, WWidth);
	public static Font GFont = new Font("Arial", Font.PLAIN, 24);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Courier managment system");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(WWidth, WHeight);
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.getContentPane().setBackground(Color.WHITE);

			JPanel sidePanel = new JPanel();
			sidePanel.setPreferredSize(SPDimension);
			sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
			sidePanel.setBorder(
				BorderFactory.createEmptyBorder(20, 10, 20, 10)
			);
			sidePanel.setBackground(SPcolor);

			frame.add(sidePanel, BorderLayout.WEST);

			String[] SPButtons = {
				"manageItems",
				"manageSessions",
				"manageDrivers",
				"manageDetails",
				"manageCustomers",
				"manageSessions",
				"manageAdmins",
				"manageBox",
			};
			for (String button : SPButtons) {
				JButton SPButton = new JButton(button);
				SPButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				sidePanel.add(SPButton);
				sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
			}
			JPanel contentPanel = new JPanel();
			contentPanel.setBackground(CPcolor);
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			contentPanel.setBorder(
				BorderFactory.createEmptyBorder(20, 200, 20, 200));
			JLabel welcomeLable = new JLabel("Content panel");
			welcomeLable.setFont(GFont);
			contentPanel.add(welcomeLable);
			frame.add(contentPanel, BorderLayout.CENTER);
	
			frame.setVisible(true);
		});
	}
}
