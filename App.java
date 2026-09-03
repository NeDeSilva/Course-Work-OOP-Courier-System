import javax.swing.*;
import java.awt.*;

/**
 * App - main entry point and single-window controller of the Courier
 * Management System.
 *
 * The whole interface lives inside one {@link JFrame}. The first screen is
 * installed before the window is made visible, and every later navigation
 * installs a fresh content panel and re-validates/repaints the window. One
 * window, one screen at a time, so an empty or stale frame is never shown.
 */
public class App {

	private static JFrame frame;

	private App() {
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				DBmanage.init();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Database init failed: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			frame = new JFrame("Courier Management System");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(640, 720);
			frame.setMinimumSize(new Dimension(480, 540));
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);

			// The first screen is placed in the window before it becomes
			// visible, so the window never appears empty.
			install(new HomeUI());

			frame.setVisible(true);
		});
	}

	/**
	 * Shows the given screen, replacing whatever was shown before. A fresh
	 * content panel is installed each time and the window is fully re-validated
	 * and repainted, so the new screen is always drawn.
	 */
	public static void open(final CoreUI screen) {
		SwingUtilities.invokeLater(() -> {
			install(screen);
			frame.validate();
			frame.repaint();
		});
	}

	private static void install(final CoreUI screen) {
		JPanel content = new JPanel(new BorderLayout());
		content.add(screen, BorderLayout.CENTER);
		frame.setContentPane(content);
		screen.requestFocusInWindow();
	}
}
