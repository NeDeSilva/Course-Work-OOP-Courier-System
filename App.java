import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

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
	public static Border borderSM = BorderFactory.createEmptyBorder(
		20,
		20,
		20,
		20
	);
	public static Border borderLA = BorderFactory.createEmptyBorder(
		20,
		100,
		20,
		100
	);
	

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
			sidePanel.setBorder(borderSM);
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
				SPButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
				sidePanel.add(SPButton);
				sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
			}
			JPanel contentPanel = new JPanel();
			contentPanel.setBackground(CPcolor);
			contentPanel.setLayout(
				new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
			);
			contentPanel.setBorder(borderLA);
			JLabel welcomeLable = new JLabel("Content panel");
			welcomeLable.setFont(GFont);
			welcomeLable.setAlignmentX(Component.CENTER_ALIGNMENT);
			frame.add(contentPanel, BorderLayout.CENTER);

			frame.setVisible(true);
		});
	}

	JPanel loginPanel() {
		JPanel LPanel = new JPanel();
		LPanel.setLayout(new BoxLayout(LPanel, BoxLayout.Y_AXIS));

		JFormattedTextField userName = new JFormattedTextField();
		userName.setBorder(borderSM);
		JPasswordField password = new JPasswordField();
		JButton signUpButton = new JButton("SIGNUP");
		signUpButton.addKeyListener(l);
		LPanel.add(signUpButton);
		LPanel.add(userName);
		LPanel.add(password);
		return LPanel;
	}

	signUpPanel(){
		JPanel SUPpanel = new JPanel();

		String[] fields = {
			"name",
			"age",
			"phone number",
			"email",
			"address",
			"government ID",
		};
		for (String field : fields) {
			JLabel filed = new JLabel(field);
			new JTextField(field);
			filed.setAlignmentX(Component.CENTER_ALIGNMENT);
			filed.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			sidePanel.add(field);
			sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
		}

		
		
	}

	void connectDB(){
		
	}
}
