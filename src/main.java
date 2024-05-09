import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingApplication app = new SortingApplication();
            app.setVisible(true);
        });
    }
}