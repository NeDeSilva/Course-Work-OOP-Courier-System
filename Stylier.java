import javax.swing.*;
import java.awt.*;

/**
 * Stylier - small helpers that build plain Swing components with the app
 * colours applied. No custom painting, no nested wrapper layouts, so the UI is
 * easy to reason about and debug.
 */
public final class Stylier {

	private Stylier() {
	}

	public static JLabel h1(String text) {
		JLabel l = new JLabel(text);
		l.setFont(Theme.H1);
		l.setForeground(Theme.TEXT);
		return l;
	}

	public static JLabel h2(String text) {
		JLabel l = new JLabel(text);
		l.setFont(Theme.H2);
		l.setForeground(Theme.TEXT);
		return l;
	}

	public static JLabel body(String text) {
		JLabel l = new JLabel(text);
		l.setFont(Theme.BODY);
		l.setForeground(Theme.TEXT);
		return l;
	}

	public static JLabel subtle(String text) {
		JLabel l = new JLabel(text);
		l.setFont(Theme.SMALL);
		l.setForeground(Theme.MUTED);
		return l;
	}

	/**
	 * A plain, standard JButton (no custom painting) with the app colours.
	 */
	public static JButton button(String text, boolean primary) {
		JButton b = new JButton(text);
		b.setFont(Theme.BUTTON);
		b.setFocusPainted(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		b.setBackground(primary ? Theme.ORANGE : Theme.WHITE);
		b.setForeground(Theme.TEXT);
		b.setOpaque(true);
		b.setBorderPainted(false);
		return b;
	}

	public static JButton backButton() {
		return button("<- Back", false);
	}

	public static JButton linkButton(String text) {
		JButton b = new JButton(text);
		b.setFont(Theme.SMALL);
		b.setForeground(Theme.ORANGE_DARK);
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return b;
	}

	public static JTextField field() {
		JTextField f = new JTextField(16);
		f.setFont(Theme.FIELD);
		f.setForeground(Theme.TEXT);
		f.setBackground(Theme.WHITE);
		f.setCaretColor(Theme.ORANGE);
		return f;
	}

	public static JPasswordField passwordField() {
		JPasswordField f = new JPasswordField(16);
		f.setFont(Theme.FIELD);
		f.setForeground(Theme.TEXT);
		f.setBackground(Theme.WHITE);
		f.setCaretColor(Theme.ORANGE);
		return f;
	}

	public static JPanel card() {
		JPanel p = new JPanel();
		p.setBackground(Theme.CARD);
		p.setBorder(BorderFactory.createLineBorder(Theme.LINE));
		return p;
	}
}
