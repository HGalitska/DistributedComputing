import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ArrayList<Monk> monks = new ArrayList<Monk>();

        int n = 10;
        boolean yan = true;
        for (int i = 0; i < n; i++) {
            if (n >= n / 2) yan = false;
            Random r = new Random();
            int energy = r.nextInt(10);
            Monk newMonk = new Monk("Monk", 45, energy, yan);
            monks.add(newMonk);
        }
        Collections.shuffle(monks);

        Duel d = new Duel(monks);
        Monk winner = new ForkJoinPool().invoke(d);

        if (winner.yan) System.out.println("Monastery Yan wins.");
        else System.out.println("Monastery In wins.");
        System.out.println("Winner: " + winner + " " + winner.energy);

        System.out.println("All:");
        for (Monk m : monks){
            System.out.println(m.name + " " + m.energy);
        }
    }
}
