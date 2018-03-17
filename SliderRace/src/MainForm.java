import javax.swing.*;

public class MainForm extends JFrame {
    private JPanel panel;
    public JSlider slider;

    public JButton startButton;
    public JButton exitButton;

    public JComboBox<Integer> priority1;
    public JComboBox<Integer> priority2;

    private void initComboBoxes() {
        for (int i = 1; i <= 10; i++) {
            priority1.addItem(i);
            priority2.addItem(i);
        }
    }

    MainForm() {
        setTitle("My First Swing Application");

        add(panel);
        initComboBoxes();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public synchronized void slideTo(int value) {
        slider.setValue(value);
        System.out.println("Slider: " + slider.getValue());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
