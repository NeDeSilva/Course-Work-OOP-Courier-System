import java.awt.*;
import javax.swing.*;

class App{
	public static void main(String[] args){
		System.out.println("hello");
			JFrame frame = new JFrame("CMS");
			frame.setSize(1000, 1000);
			frame.setLayout(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

			HomeUI homeUI = new HomeUI();
			frame.add(homeUI);

			JLabel label = new JLabel("Courier Management System");
			label.setBounds(400, 50, 200, 50);
			label.addNotify();
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setForeground(Color.blue);
			label.setFont(new Font("Arial", Font.BOLD, 24));
			frame.add(label);
		}
}