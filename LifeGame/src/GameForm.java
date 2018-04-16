import javax.swing.*;
import java.awt.*;

public class GameForm extends JFrame {
    private JPanel fieldGrid;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox<Integer> comboBox;

    GameForm(int fieldWidth, int fieldHeight, int cellSize) {
        super("Game Of Life");
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //------------------------------------------------------------------FIELD GRID
        fieldGrid = new FieldGrid(fieldWidth, fieldHeight, cellSize);
        add(fieldGrid);
        //----------------------------------------------------------------TOOLBAR
        JToolBar tools = new JToolBar();
        add(tools, BorderLayout.EAST);

        startButton = new JButton("Start");
        tools.add(startButton);
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        tools.add(stopButton);

        comboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        tools.add(comboBox);

        //------------------------------------------------------------------ACTION LISTENERS
        startButton.addActionListener(e -> {
            if (comboBox.getSelectedIndex() != -1) {
                int numberOfGangs = comboBox.getItemAt(comboBox.getSelectedIndex());
                ((FieldGrid)fieldGrid).startLife(numberOfGangs);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });

        stopButton.addActionListener(e -> {
            ((FieldGrid)fieldGrid).stopLife();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        pack();
        setVisible(true);

    }
}
