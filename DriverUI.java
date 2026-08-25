import javax.swing.*;
import java.awt.*;

public class DriverUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public DriverUI(Driver d) {
        super("Driver: " + d.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panel.add(new JLabel("Name: " + d.getName()));
        panel.add(new JLabel("Gov ID: " + d.getGovID()));
        panel.add(new JLabel("Age: " + d.getAge()));
        panel.add(new JLabel("Address: " + d.getAddress()));
        panel.add(new JLabel("Phone: " + d.getPhoneNumber()));
        panel.add(new JLabel("Email: " + d.getEmailAddress()));
        panel.add(new JLabel("License: " + d.getLicenseNumber()));
        panel.add(new JLabel("Vehicle: " + d.getVehicleNumber()));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        panel.add(close);

        add(panel);
        setVisible(true);
    }

    public static void show(Driver d) {
        SwingUtilities.invokeLater(() -> new DriverUI(d));
    }
}