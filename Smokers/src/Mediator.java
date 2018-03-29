import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Mediator extends Thread {
    private String name;
    private ArrayList<Boolean> table;

    private Semaphore s;

    Mediator(String name, ArrayList<Boolean> table, Semaphore s) {
        this.name = name;
        this.table = table;
        this.s = s;
    }

    private void putItemsOnTable() {
        Random r = new Random();
        int firstItem = r.nextInt(3);
        int secondItem = r.nextInt(3);
        while (firstItem == secondItem) secondItem = r.nextInt(3);

        table.set(firstItem, true);
        table.set(secondItem, true);

        System.out.println(name + " put item " + firstItem + " and " + secondItem + " on table.");
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            synchronized (table) {
                putItemsOnTable();
                table.notifyAll();
            }
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
