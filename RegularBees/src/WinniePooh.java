import java.util.concurrent.Semaphore;

public class WinniePooh extends Thread {
    private Semaphore semaphore;
    private HoneyPot pot;

    boolean isFat = false;

    WinniePooh(Semaphore semaphore, HoneyPot pot) {
        this.semaphore = semaphore;
        this.pot = pot;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            // wait for pot to become full
            try {
                synchronized (pot) {
                    while (!pot.isFull()) {
                        pot.wait();
                    }
                }
                semaphore.acquire();
                pot.eatWhole();
                drawEating();
                System.out.println("!!!! Winnie has eaten all honey.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // honey is eaten, release semaphore
            finally {
                semaphore.release();
            }
        }
        isFat = true;
        System.out.println("Winnie is fat and doesn't want to eat any more.");
    }

    private void drawEating() {
        System.out.println(" __( )_\n" +
                "(      (o____\n" +
                " |          |\n" +
                " |      (__/\n" +
                "   \\     /   ___\n" +
                "   /     \\  \\___/\n" +
                " /    ^    /     \\\n" +
                "|   |  |__|_HUNNY |\n" +
                "|    \\______)____/\n" +
                " \\         /\n" +
                "   \\     /_\n" +
                "    |  ( __)\n" +
                "    (____)");
    }
}
