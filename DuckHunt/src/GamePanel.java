import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {

    Timer timer;
    final int DELAY = 10;
    Image backgroundImage;
    Duck duck;

    public GamePanel() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        //setLayout(null);
        backgroundImage = getImageIcon("background.png").getImage();

        int w = backgroundImage.getWidth(this);
        int h = backgroundImage.getHeight(this);
        setPreferredSize(new Dimension(w, h));

        duck = new Duck(new Position(0, 0), new Speed(1, 1), true);
        timer = new Timer(DELAY, this);
        timer.start();
    }

    ImageIcon getImageIcon(String path) {
        return new ImageIcon(path);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(duck.aliveImage, duck.position.x, duck.position.y, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        step();
    }

    private void step() {

        duck.move();

        repaint(duck.position.x, duck.position.y,
                duck.w, duck.h);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            duck.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            duck.keyPressed(e);
        }
    }
}
