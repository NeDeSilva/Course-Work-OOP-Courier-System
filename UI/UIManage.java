package UI;

import java.awt.LayoutManager;

import javax.swing.*;

public class UIManage {

	public UIManage() {
		SwingUtilities.invokeLater(() -> {
			createUI();
		});
	}

	private static void createUI() {
		JFrame frame = new JFrame("Courier management system");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new LayoutManager() {
			
		});
		frame.setVisible(true);
		frame.add(UIPanels.createHomePanel());
		frame.add(UIPanels.createTopPanel());
		frame.revalidate();
		frame.repaint();
	}
}
