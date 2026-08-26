import javax.swing.*;

public class CoreUIPanels {
	
	public static JPanel createLoginPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JButton loginButton = CoreUIElements.createButton("Login");
		loginButton.setBounds(150, 200, 100, 30);
		panel.add(loginButton);
		return panel;
	}
}