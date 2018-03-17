import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {
    private JPanel panel;

    public JSlider slider;

    public JButton Stop2Button;
    public JButton Start1Button;
    public JButton Start2Button;
    public JButton Stop1Button;

    public JTextPane textPane;

    MainForm() {
        Stop1Button.setEnabled(false);
        Stop2Button.setEnabled(false);

        setTitle("My First Swing Application");

        add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void slideTo(int value) {
        synchronized (slider) {
            slider.setValue(value);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
