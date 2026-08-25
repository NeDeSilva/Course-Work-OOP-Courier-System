import javax.swing.*;
import java.awt.*;

public class PrototypeLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppController controller = new AppController();

            JFrame frame = new JFrame("Prototype — Inventory & Users");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000,700);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            InventoryPanel inv = new InventoryPanel(controller);
            UsersPanel users = new UsersPanel(controller);
            tabs.addTab("Inventory", inv);
            tabs.addTab("Users", users);

            frame.add(tabs, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}