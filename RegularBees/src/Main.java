import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    static int n = 10; // number of bees

    public static void main(String[] args) {
        int potCapacity = 5; // pot size
        HoneyPot pot = new HoneyPot(potCapacity); // shared honey pot
        Semaphore semaphore = new Semaphore(1); // semaphore for shared usage of pot

        WinniePooh winnie = new WinniePooh(semaphore, pot); // Winnie Pooh
        winnie.start();

        ArrayList<Bee> bees = new ArrayList<>();
        for (int i = 0; i < n; i++){
            Bee b = new Bee("Bee " + i, semaphore, winnie, pot);
            bees.add(b);
        }

        for (Bee b : bees){
            b.start();
        }

        // wait for bees to finish
        for (Bee b : bees){
            try {
                b.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // wait for Winnie to finish
        try {
            winnie.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
