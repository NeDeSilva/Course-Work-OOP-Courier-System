import javax.swing.*;
import java.awt.*;

public class CustomerUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public CustomerUI(Customer c) {
        super("Customer: " + c.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 280);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panel.add(new JLabel("Name: " + c.getName()));
        panel.add(new JLabel("Gov ID: " + c.getGovID()));
        panel.add(new JLabel("Age: " + c.getAge()));
        panel.add(new JLabel("Address: " + c.getAddress()));
        panel.add(new JLabel("Phone: " + c.getPhoneNumber()));
        panel.add(new JLabel("Email: " + c.getEmailAddress()));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        panel.add(close);

        add(panel);
        setVisible(true);
    }

    public static void show(Customer c) {
        SwingUtilities.invokeLater(() -> new CustomerUI(c));
    }
}