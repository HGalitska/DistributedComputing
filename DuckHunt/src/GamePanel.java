import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    final static int NUMBER_OF_DUCKS = 10;
    private Timer timer;
    boolean inGame = false;
    Image backgroundImage;

    static int GAME_WIDTH;
    static int GAME_HEIGHT;


    private Hunter hunter;
    private java.util.List<Duck> ducks;

    public GamePanel() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        inGame = true;

        backgroundImage = new ImageIcon("background.png").getImage();

        GAME_WIDTH = backgroundImage.getWidth(this);
        GAME_HEIGHT = backgroundImage.getHeight(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));


        hunter = new Hunter(0, GAME_HEIGHT - 300, 0, 0);
        new Thread(hunter).start();

        initDucks();

        timer = new Timer(10, this);
        timer.start();
    }

    public void initDucks() {
        ducks = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_DUCKS; i++) {
            Random r = new Random();
            int x = r.nextInt(GAME_WIDTH - 300);
            int y = r.nextInt(GAME_HEIGHT - 600);
            int dx = r.nextInt(2) + 1;

            Duck newDuck = new Duck(x, y, dx, 0, r.nextBoolean());
            new Thread(newDuck).start();
            ducks.add(newDuck);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);

        if (inGame) {
            drawObjects(g);
        } else {
            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    void drawObjects(Graphics g) {
        if (hunter.isVisible()) {
            g.drawImage(hunter.getImage(), hunter.getX(), hunter.getY(), this);
        }

        ArrayList<Shot> shots = (ArrayList) hunter.shots;

        for (Shot s : shots) {
            if (s.isVisible()) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }

        for (Duck duck : ducks) {
            if (duck.isVisible()) {
                g.drawImage(duck.getImage(), duck.getX(), duck.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Ducks left: " + ducks.size(), 5, 15);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (GAME_WIDTH - fm.stringWidth(msg)) / 2,
                GAME_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inGame();

        //updateHunter();
        updateShots();
        updateDucks();

        checkCollisions();

        repaint();
    }

    private void inGame() {

        if (!inGame) {
            timer.stop();
        }
    }

    /*
    private void updateHunter() {

        if (hunter.isVisible()) {
            hunter.move();
        }
    }
    */

    private void updateShots() {
        java.util.List<Shot> ms = hunter.shots;

        for (int i = 0; i < ms.size(); i++) {

            Shot m = ms.get(i);

            if (m.isVisible()) {
                m.move();
            } else {
                ms.remove(i);
            }
        }
    }

    private void updateDucks() {

        if (ducks.isEmpty()) {
            inGame = false;
            return;
        }

        for (int i = 0; i < ducks.size(); i++) {

            Duck a = ducks.get(i);

            if (!a.isVisible()) {
                ducks.remove(i);
            }
        }
    }

    public void checkCollisions() {
        for (Shot shot : hunter.shots) {
            Rectangle r2 = shot.getBounds();
            for (Duck duck : ducks) {

                Rectangle r1 = duck.getBounds();

                if (r1.intersects(r2)) {
                    shot.setVisible(false);
                    duck.setVisible(false);
                    if (ducks.size() == 0) inGame = false;
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                hunter.dx = 0;
            }

            if (key == KeyEvent.VK_RIGHT) {
               hunter.dx = 0;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                hunter.fire();
            }

            if (key == KeyEvent.VK_LEFT) {
                hunter.dx = -1;
            }

            if (key == KeyEvent.VK_RIGHT) {
                hunter.dx = 1;
            }
        }
    }
}