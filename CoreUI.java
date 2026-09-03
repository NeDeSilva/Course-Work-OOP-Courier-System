import javax.swing.*;
import java.awt.*;

/**
 * CoreUI - base class for every screen.
 *
 * A plain JPanel with a simple BorderLayout: a title bar on top, the screen's
 * content in the middle, and a back button at the bottom. Screens are swapped
 * by {@link App} inside a single window.
 */
public abstract class CoreUI extends JPanel {

	protected JPanel body;
	protected JButton backBtn;

	public CoreUI() {
		setLayout(new BorderLayout());
		setBackground(Theme.BG);

		add(titleBar(), BorderLayout.NORTH);

		// Simple centred body: content is placed in the middle and grows to fit.
		body = new JPanel(new BorderLayout());
		body.setBackground(Theme.BG);
		add(body, BorderLayout.CENTER);

		add(footerBar(), BorderLayout.SOUTH);
	}

	private JPanel titleBar() {
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(Theme.YELLOW_SOFT);
		top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
		JLabel title = Stylier.h2("Courier Management System");
		title.setForeground(Theme.ORANGE_DARK);
		top.add(title, BorderLayout.WEST);
		return top;
	}

	private JPanel footerBar() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		footer.setBackground(Theme.YELLOW_SOFT);
		backBtn = Stylier.backButton();
		backBtn.setVisible(false);
		footer.add(backBtn);
		return footer;
	}

	/**
	 * Subclasses build their content into {@link #body}.
	 */
	protected void buildBody() {
	}

	protected void showCard(JComponent content) {
		content.setOpaque(true);
		body.add(content, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	protected void goTo(final CoreUI next) {
		App.open(next);
	}

	protected void linkBack(final CoreUI previous) {
		backBtn.setVisible(true);
		backBtn.addActionListener(e -> goTo(previous));
	}
}
