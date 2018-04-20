import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Hunter extends GameObject implements Runnable{
    List<Shot> shots;

    Hunter(int x, int y, int dx, int dy) {
        super(x, y, dx, dy);

        loadImage("hunter.png");
        getImageDimensions();

        shots = new ArrayList<>();
    }

    @Override
    public void run() {
        while(visible) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x += dx * 10;
        }
    }

    public void fire() {
        Shot newShot = new Shot(x + width - 3, y + 10);
        shots.add(newShot);
    }
}
