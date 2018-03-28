import java.util.concurrent.Semaphore;

public class Bee extends Thread {
    String name;
    private Semaphore semaphore;
    private WinniePooh winnie;
    private HoneyPot pot;

    Bee(String name, Semaphore semaphore, WinniePooh winnie, HoneyPot pot) {
        this.name = name;
        this.semaphore = semaphore;
        this.winnie = winnie;
        this.pot = pot;
    }

    @Override
    public void run() {
        while (!winnie.isFat) {
            try {
                semaphore.acquire();
                if (!pot.isFull() && !winnie.isFat) {
                    pot.put(1);
                    System.out.println(name + " put in pot.");
                    if (pot.isFull()) {
                        synchronized (pot) {
                            pot.notify();
                            System.out.println("Winnie is notified by " + name);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}
