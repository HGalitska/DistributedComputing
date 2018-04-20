public class Shot extends GameObject{
    public Shot(int x, int y) {
        super(x, y, 0, 2);
        dy = 5;

        loadImage("shot.png");
        getImageDimensions();

    }

    public void move() {

        y -= dy;

        if (y < 0)
            visible = false;
    }
}
