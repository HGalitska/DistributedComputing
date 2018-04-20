import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Math.abs;

public class Duck extends GameObject implements Runnable{
    boolean direction;

    Duck(int x, int y, int dx, int dy, boolean direction) {
        super(x, y, dx, dy);
        this.direction = direction;

        loadImage("duck.gif");
        width = 200;
        height = 100;
    }

    @Override
    public void run() {
        while(visible) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (direction) {
                loadImage("duck.gif");
                x += dx;
            }
            else
            {
                loadImage("duck_r.gif");
                x -= dx;
            }

            if (x > GamePanel.GAME_WIDTH - 200 || x < 0) direction = !direction;
        }
    }
}
