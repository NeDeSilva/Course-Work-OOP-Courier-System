import javax.swing.*;
import java.awt.*;

public class PrototypeLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showLogin(new AppController()));
    }

    private static void showLogin(AppController controller) {
        JFrame loginFrame = new JFrame("Login — Courier Prototype");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LoginPanel lp = new LoginPanel(controller, user -> {
            loginFrame.dispose();
            SwingUtilities.invokeLater(() -> openMainFrame(controller, user));
        });
        loginFrame.add(lp);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void openMainFrame(AppController controller, Person user) {
        JFrame main = new JFrame("Prototype — " + user.getClass().getSimpleName() + " view");
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main.setSize(1000,700);
        main.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        InventoryPanel inv = new InventoryPanel(controller);
        UsersPanel users = new UsersPanel(controller);
        CustomerPanel customerPanel = new CustomerPanel(controller);
        SellerPanel sellerPanel = new SellerPanel(controller);
        DriverPanel driverPanel = new DriverPanel(controller);

        String role = user.getClass().getSimpleName().toLowerCase();
        if (role.equals("admin")) {
            tabs.addTab("Inventory", inv);
            tabs.addTab("Users", users);
            tabs.addTab("Customer", customerPanel);
            tabs.addTab("Seller", sellerPanel);
            tabs.addTab("Driver", driverPanel);
        } else if (role.equals("seller")) {
            tabs.addTab("Seller", sellerPanel);
            tabs.addTab("Inventory", inv);
        } else if (role.equals("driver")) {
            tabs.addTab("Driver", driverPanel);
        } else if (role.equals("customer")) {
            tabs.addTab("Customer", customerPanel);
        } else {
            // fallback: show Inventory + Users
            tabs.addTab("Inventory", inv);
            tabs.addTab("Users", users);
        }

        JButton logout = new JButton("Logout");
        logout.addActionListener(a -> {
            main.dispose();
            SwingUtilities.invokeLater(() -> showLogin(controller));
        });

        main.add(tabs, BorderLayout.CENTER);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(new JLabel("Logged in: " + user.getName() + " (" + user.getClass().getSimpleName() + ")"));
        top.add(logout);
        main.add(top, BorderLayout.NORTH);

        main.setVisible(true);
    }
}