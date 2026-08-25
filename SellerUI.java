import javax.swing.*;
import java.awt.*;

public class SellerUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public SellerUI(Seller s) {
        super("Seller: " + s.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panel.add(new JLabel("Name: " + s.getName()));
        panel.add(new JLabel("Gov ID: " + s.getGovID()));
        panel.add(new JLabel("Age: " + s.getAge()));
        panel.add(new JLabel("Address: " + s.getAddress()));
        panel.add(new JLabel("Phone: " + s.getPhoneNumber()));
        panel.add(new JLabel("Email: " + s.getEmailAddress()));
        panel.add(new JLabel("Shop: " + s.getShopName()));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        panel.add(close);

        add(panel);
        setVisible(true);
    }

    public static void show(Seller s) {
        SwingUtilities.invokeLater(() -> new SellerUI(s));
    }
}