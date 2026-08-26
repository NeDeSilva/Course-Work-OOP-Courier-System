/**
 * CoreUI
 */

import java.awt.*;
import javax.swing.*;

class CoreUIElements {

	static String title = "Courier Management System";
	static int baseLocationX = 100;
	static int baseLocationY = 100;
	int windowHeight = 700;
	int windowWidth = 700;
	int sectionHeight = 30;
	int sectionWidth = 50;

	static int elementHeight01 = 80;
	static int elementWidth01 = 160;
	int elementHeight02 = 30;
	int elementWidth02 = 70;
	int elementHeight03 = 20;
	int elementWidth03 = 60;

	static Color backgroundColor = new Color(79, 70, 229);
	static Color elementColor = new Color(233, 00, 00);
	static Color textColor = new Color(45, 00, 00);
	static Font myFont = new Font("Segoe UI",Font.BOLD, 14);
	
	static JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setBounds(10, 10, elementWidth01, elementHeight01);
	 button.setBackground(new Color(79, 70, 229)); // #4F46E5 - Modern Indigo
    button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		//button.setBorderPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		//button.setContentAreaFilled(false);
		button.setFont(myFont);

	 button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(99, 102, 241)); // Light Indigo on Hover
        }
	
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(79, 70, 229)); // Original Indigo
        }
	
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(55, 48, 163)); // Darker Indigo on Click
        }
	
        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(99, 102, 241));
        }
	});
		return button;
	}

	static JLabel createLabel(String name) {
		JLabel label = new JLabel(name);
		@Override
		protected void paintComponent(Graphics g){
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                               RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                               RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintComponent(g2);
            g2.dispose();
		}

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
