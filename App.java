import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CoreUI frame = new CoreUI();
            frame.setVisible(true);
        });
    }
}
