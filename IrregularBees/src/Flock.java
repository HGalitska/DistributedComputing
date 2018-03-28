import java.util.ArrayList;

public class Flock extends Thread {
    private int id;


    Flock(int id) {
        this.id = id;
    }

    int getTask() {
        for (int i = 0; i < Main.forest.size(); i++) {
            if (!Main.searchedSectors.get(i)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run() {
        int sector_index = getTask();
        if (sector_index == -1) return;

        ArrayList<Boolean> sector = Main.forest.get(sector_index);
        Main.searchedSectors.set(sector_index, true);

        System.out.println("#" + id + " ---> sector " + sector_index);

        if (Main.foundBear) return;

        int current_cell = 0;
        while (true) {
            System.out.println("#" + id + " ---> next cell.");
            if (current_cell == sector.size()) {
                System.out.println("#" + id + " failed.");
                getTask();
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
