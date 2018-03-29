import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Smoker extends Thread {
    private String name;
    private int myItem;
    private ArrayList<Boolean> table;

    private Semaphore s;

    Smoker(String name, int item, ArrayList<Boolean> table, Semaphore s) {
        this.name = name;
        this.myItem = item;
        this.table = table;
        this.s = s;
    }

    boolean tryToTakeItems() {
        for (int i = 0; i < table.size(); i++) {
            if (i != myItem && !table.get(i)) return false;
        }
        for (int i = 0; i < table.size(); i++) {
            if (i != myItem && table.get(i)) {
                System.out.println(name + " has taken item " + i);
                table.set(i, false);
            }
        }
        return true;
    }

    void smoke() {
        System.out.println(name + " is smoking...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " finished...");
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (table) {
                    table.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                s.acquire();

                if (tryToTakeItems()) {
                    smoke();
                    synchronized (Main.Jocker) {
                        Main.Jocker.notify();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                s.release();
            }
        }
    }
}
