import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Duck {
    Position position;
    Speed speed;
    boolean direction;
    Image aliveImage;

    int w, h;

    Duck() {
        loadImages();
    }
    Duck(Position position, Speed speed, boolean direction) {
        this.position = position;
        this.speed = speed;
        this.direction = direction;

        loadImages();
    }

    private void loadImages() {
        ImageIcon ii1 = new ImageIcon("duck.gif");
        aliveImage = ii1.getImage();

        w = aliveImage.getWidth(null);
        h = aliveImage.getHeight(null);
    }

    public void move() {
        position.x += speed.dx;
        position.y += speed.dy;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            speed.dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            speed.dx = 2;
        }

        if (key == KeyEvent.VK_UP) {
            speed.dy = -2;
        }

        if (key == KeyEvent.VK_DOWN) {
            speed.dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            speed.dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            speed.dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            speed.dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            speed.dy = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, w, h);
    }
}
