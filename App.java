import javax.swing.*;

class App{
	public static void main(String[] args){
		SwingUtilities.invokeLater(() -> {createAndShowGUI();});			
		}

	private static void createAndShowGUI(){
		JFrame frame = new JFrame("CMS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setVisible(true);

		JPanel loginPanel = CoreUIPanels.createLoginPanel();
		frame.add(loginPanel);
		
		JButton loginButton = CoreUIElements.createButton("Login");
		loginButton.setBounds(150, 200, 100, 30);
		frame.add(loginButton);
	}
}