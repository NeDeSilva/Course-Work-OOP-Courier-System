import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Top-level application frame.
 * Shows a login card first; after a successful login it swaps to a
 * role-aware dashboard using a {@link CardLayout}.
 */
public class CoreUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final AppController controller = new AppController();
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final JLabel loggedIn = new JLabel();

    private Person currentUser;

    public CoreUI() {
        super("Courier Management System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(960, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BACKGROUND);

        root.add(wrapCentered(new LoginPanel(controller, this::onLogin)), "login");
        setContentPane(root);
    }

    /** Puts a component on a light-grey centered background. */
    private static JPanel wrapCentered(JComponent inner) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BACKGROUND);
        wrapper.add(inner);
        return wrapper;
    }

    private void onLogin(Person user) {
        this.currentUser = user;
        root.add(buildDashboard(user), "dashboard");
        cards.show(root, "dashboard");
    }

    private JPanel buildDashboard(Person user) {
        loggedIn.setText(user.getName() + "  •  " + roleOf(user));

        JPanel dash = new JPanel(new BorderLayout(0, 10));
        dash.setBackground(UITheme.BACKGROUND);
        dash.add(topBar(), BorderLayout.NORTH);
        dash.add(buildTabs(user), BorderLayout.CENTER);
        return dash;
    }

    private JPanel topBar() {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(UITheme.PRIMARY);
        top.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel brand = new JLabel("Courier Management System");
        brand.setFont(UITheme.FONT_TITLE);
        brand.setForeground(Color.WHITE);
        top.add(brand, BorderLayout.WEST);

        loggedIn.setFont(UITheme.FONT_SUB);
        loggedIn.setForeground(Color.WHITE);
        loggedIn.setBorder(new EmptyBorder(0, 8, 0, 8));

        JButton saveBtn = UITheme.secondaryButton("Save");
        saveBtn.addActionListener(e -> saveAll());
        JButton logoutBtn = UITheme.dangerButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(loggedIn);
        actions.add(saveBtn);
        actions.add(logoutBtn);
        top.add(actions, BorderLayout.EAST);
        return top;
    }

    private JTabbedPane buildTabs(Person user) {
        JTabbedPane tabs = new JTabbedPane();
        if (user instanceof Admin) {
            tabs.addTab("Inventory", new InventoryPanel(controller));
            tabs.addTab("Users", new UsersPanel(controller));
        } else if (user instanceof Seller) {
            tabs.addTab("My Products", new SellerPanel(controller));
        } else if (user instanceof Driver) {
            tabs.addTab("My Deliveries", new DriverPanel(controller));
        } else {
            tabs.addTab("My Shipments", new CustomerPanel(controller));
        }
        tabs.addTab("Shipments", new ShipmentsPanel(controller));
        return tabs;
    }

    private void saveAll() {
        boolean ok = controller.saveAll();
        JOptionPane.showMessageDialog(this,
                ok ? "All data saved." : "Save failed.",
                "Save", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private void logout() {
        this.currentUser = null;
        root.removeAll();
        root.add(wrapCentered(new LoginPanel(controller, this::onLogin)), "login");
        cards.show(root, "login");
        revalidate();
        repaint();
    }

    private static String roleOf(Person user) {
        return user.getClass().getSimpleName();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CoreUI().setVisible(true));
    }
}
