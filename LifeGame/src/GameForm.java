import javax.swing.*;
import java.awt.*;

public class GameForm extends JFrame {
    private JPanel fieldGrid;
    private JButton startButton;
    private JButton stopButton;

    GameForm(int fieldWidth, int fieldHeight, int cellSize) {
        super("Game Of Life");
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

        //------------------------------------------------------------------ACTION LISTENERS
        startButton.addActionListener(e -> {
            ((FieldGrid) fieldGrid).startLife(3);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.addActionListener(e -> {
            ((FieldGrid) fieldGrid).stopLife();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        pack();
        setVisible(true);

    }
}
