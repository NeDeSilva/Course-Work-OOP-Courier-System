import javax.swing.*;
import java.awt.*;

/**
 * Shared base for read-only "person detail" dialogs.
 * Concrete subclasses only describe which fields to show, keeping the
 * visual layout in one place.
 */
public abstract class PersonDetailUI extends JFrame {
    private static final long serialVersionUID = 1L;

    protected PersonDetailUI(Person person, int width, int height) {
        super(person.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BACKGROUND);

        JPanel panel = new JPanel();
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.setLayout(new BorderLayout());

        JPanel body = UITheme.card();
        body.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel title = UITheme.header(person.getName());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(title);
        addSeparator(body);

        addDetailRows(body, person);

        JButton close = UITheme.secondaryButton("Close");
        close.setAlignmentX(Component.LEFT_ALIGNMENT);
        close.addActionListener(e -> dispose());
        body.add(Box.createVerticalStrut(14));
        body.add(close);

        panel.add(body, BorderLayout.CENTER);
        add(panel);
    }

    /** Subclasses add their specific fields here. */
    protected abstract void addDetailRows(JPanel panel, Person person);

    protected final void addRow(JPanel panel, String key, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel k = UITheme.label(key);
        k.setForeground(UITheme.TEXT_MUTED);
        row.add(k, BorderLayout.WEST);
        row.add(UITheme.label(value == null || value.isEmpty() ? "—" : value), BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        panel.add(row);
    }

    protected final void addSeparator(JPanel panel) {
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(10));
    }
}
