import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Centralised modern look &amp; feel for the whole application.
 * Keeps colours, fonts and common component wiring in one place so that
 * individual panels stay small and consistent (single responsibility +
 * DRY).
 */
public final class UITheme {

    /* ---- Palette ---- */
    public static final Color PRIMARY = new Color(0x4F46E5);      // indigo
    public static final Color PRIMARY_DARK = new Color(0x3730A3);
    public static final Color PRIMARY_LIGHT = new Color(0x6366F1);
    public static final Color ACCENT = new Color(0x06B6D4);       // cyan
    public static final Color SUCCESS = new Color(0x16A34A);
    public static final Color DANGER = new Color(0xDC2626);
    public static final Color BACKGROUND = new Color(0xF4F6FB);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(0xE2E8F0);
    public static final Color TEXT = new Color(0x1F2937);
    public static final Color TEXT_MUTED = new Color(0x64748B);

    /* ---- Fonts ---- */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 13);

    private static final Dimension PAD = new Dimension(18, 8);

    private UITheme() {
        throw new AssertionError("Utility class");
    }

    /** Flat, modern primary button with hover states. */
    public static JButton primaryButton(String text) {
        return styledButton(text, PRIMARY, PRIMARY_LIGHT, PRIMARY_DARK);
    }

    /** Flat success button (green). */
    public static JButton successButton(String text) {
        return styledButton(text, SUCCESS, SUCCESS.brighter(), SUCCESS.darker());
    }

    /** Flat danger button (red). */
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, DANGER.brighter(), DANGER.darker());
    }

    /** Neutral secondary/outline button. */
    public static JButton secondaryButton(String text) {
        return styledButton(text, new Color(0xE2E8F0), new Color(0xF1F5F9), new Color(0xCBD5E1));
    }

    private static JButton styledButton(String text, Color base, Color hover, Color pressed) {
        JButton b = new JButton(text);
        b.setFont(FONT_BUTTON);
        b.setBackground(base);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(PAD);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(base); }
            @Override public void mousePressed(java.awt.event.MouseEvent e){ b.setBackground(pressed); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e){ b.setBackground(hover); }
        });
        return b;
    }

    /** A white card panel with a soft border for grouping content. */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createLineBorder(BORDER));
        p.setLayout(new BorderLayout(12, 12));
        return p;
    }

    /** Header text for sections. */
    public static JLabel header(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADER);
        l.setForeground(TEXT);
        return l;
    }

    /** Muted body label. */
    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SUB);
        l.setForeground(TEXT);
        return l;
    }

    /** Consistent styled text field. */
    public static JTextField field(int columns) {
        JTextField t = new JTextField(columns);
        t.setFont(FONT_FIELD);
        t.setPreferredSize(new Dimension(40 * columns, 32));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(4, 8, 4, 8)));
        return t;
    }

    /** Consistent styled password field. */
    public static JPasswordField passwordField(int columns) {
        JPasswordField t = new JPasswordField(columns);
        t.setFont(FONT_FIELD);
        t.setPreferredSize(new Dimension(40 * columns, 32));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(4, 8, 4, 8)));
        return t;
    }

    /** Table styling shared across the app. */
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setFont(FONT_SUB);
        table.setForeground(TEXT);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.getTableHeader().setFont(FONT_BUTTON);
        table.getTableHeader().setBackground(new Color(0xEEF2FF));
        table.getTableHeader().setForeground(PRIMARY_DARK);
        table.getTableHeader().setReorderingAllowed(false);
    }
}
