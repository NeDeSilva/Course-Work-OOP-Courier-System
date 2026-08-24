import javax.swing.*;
import java.awt.*;

class HomeUI extends CoreUI {
	HomeUI() {
		setLayout(null);
		setBackground(backgroundColor);
		
		JLabel titleLabel = new JLabel("hello");
		titleLabel.setBounds(100, 100, 100, 100);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(textColor);
		add(titleLabel);
		
		JButton btnDashboard = new JButton("Dashboard");
		btnDashboard.setBounds(100, 100, 300, 300);
		btnDashboard.setBackground(elementColor);
		btnDashboard.setForeground(textColor);
		add(btnDashboard);
	}
}