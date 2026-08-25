/**
 * CoreUI
 */

import java.awt.*;
import javax.swing.*;

class CoreUIElements extends JPanel {

	String title = "Courier Management System";
	int baseLocationX = 100;
	int baseLocationY = 100;
	int windowHeight = 700;
	int windowWidth = 700;
	int sectionHeight = 30;
	int sectionWidth = 50;

	int elementHeight01 = 40;
	int elementWidth01 = 80;
	int elementHeight02 = 30;
	int elementWidth02 = 70;
	int elementHeight03 = 20;
	int elementWidth03 = 60;

	Color backgroundColor = Color.WHITE;
	Color elementColor = Color.ORANGE;
	Color textColor = Color.GRAY;
	
	JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setBounds(10, 10, elementWidth01, elementHeight01);
		button.setBackground(elementColor);
		button.setForeground(textColor);
		return button;
	}

	JLabel createLabel() {
		JLabel label = new JLabel();
		label.setBounds(100, 100, 100, 100);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(textColor);
		return label;
	}

	JTextField createTextField(String name) {
		JTextField textField = new JTextField(name);
		textField.setBounds(10, 10, elementWidth02, elementHeight02);
		textField.setBackground(elementColor);
		textField.setForeground(textColor);
		return textField;
	}

}
