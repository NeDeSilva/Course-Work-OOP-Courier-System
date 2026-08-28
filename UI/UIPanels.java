package UI;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.*;

public class UIPanels {
	
	public static JPanel createLoginPanel() {
		JPanel panel = new JPanel();
		//panel.setLayout(null);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBounds(0, 0, 400, 300);
		panel.setBackground(Color.BLACK);
		JButton loginButton = UIElements.createButton("Login");
		loginButton.setBounds(0, 0, UIProperties.windowWidth, UIProperties.windowHeight);
		loginButton.setOpaque(true);
		loginButton.setBackground(Color.BLUE);
		panel.setBackground(Color.BLACK);
		panel.add(loginButton);
		JButton loginButton2 = UIElements.createButton("Login");
		loginButton2.setBounds(150, 200, 100, 30);
		loginButton2.setOpaque(true);
		loginButton2.setBackground(Color.BLUE);
		panel.setBackground(Color.BLACK);
		panel.add(loginButton2);
		return panel;
	}

	public static JPanel createHomePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBounds(0, 100, UIProperties.windowWidth, UIProperties.windowHeight);
		panel.setBackground(UIProperties.backgroundColor);
		JLabel welcomeLabel = UIElements.createLabel("Welcome Home");
		welcomeLabel.setBounds(100, 100, 400, 30);
		panel.add(welcomeLabel);
		JButton logoutButton = UIElements.createButton("Logout");
		logoutButton.setBounds(0, 0, UIProperties.elementWidth01, UIProperties.elementHeight01);
		logoutButton.setOpaque(true);
		logoutButton.setBackground(Color.RED);
		panel.add(logoutButton);
		return panel;
	}

	public static JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, UIProperties.windowWidth, 100);
		panel.setBackground(Color.CYAN);
		return panel;
	}

	public static JPanel createAdminPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel adminLabel = UIElements.createLabel("Admin Panel");
		adminLabel.setBounds(100, 100, 200, 30);
		panel.add(adminLabel);
		return panel;
	}

	public static JPanel createDriverPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel courierLabel = UIElements.createLabel("Courier Panel");
		courierLabel.setBounds(100, 100, 200, 30);
		panel.add(courierLabel);
		return panel;
	}

	public static JPanel createCustomerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel customerLabel = UIElements.createLabel("Customer Panel");
		customerLabel.setBounds(100, 100, 200, 30);
		panel.add(customerLabel);
		return panel;
	}

	public static JPanel createSellerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel sellerLabel = UIElements.createLabel("Seller Panel");
		sellerLabel.setBounds(100, 100, 200, 30);
		panel.add(sellerLabel);
		return panel;
	}

	public static JPanel creatAccountPanel(){
		JPanel panel = new JPanel();
		JLabel field1 = UIElements.createLabel("hi");
		panel.add(field1);
		return panel;
	}
}