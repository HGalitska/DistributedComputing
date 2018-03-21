import java.util.ArrayList;

public class Flock extends Thread {
    private int id;
    private ArrayList<Boolean> sector;

    Flock(int id, ArrayList<Boolean> sector) {
        this.id = id;
        this.sector = sector;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int current_cell = 0;
        while (true) {
            System.out.println("#" + id + " ---> next cell.");
            if (current_cell == sector.size()) {
                System.out.println("#" + id + " failed.");
                Thread.currentThread().interrupt();
                return;
            }
            boolean here = sector.get(current_cell);
            if (here) {
                Winnie.print();
                System.out.println("<!!!> #" + id + " found and punished Winnie, returned. <!!!>");
                Main.foundBear = true;
                Thread.currentThread().interrupt();
                return;
            }
            current_cell++;
        }
    }
}
