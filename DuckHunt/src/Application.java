import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {
    public Application() {
        initUI();
    }

    private void initUI() {
        add(new GamePanel());

        setResizable(false);
        pack();

        setTitle("Duck Hunt");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application DuckHunt = new Application();
            DuckHunt.setVisible(true);
        });
    }
}
