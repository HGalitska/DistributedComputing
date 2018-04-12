import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Main {
    static CyclicBarrier barrier;
    static List<AsBs> partialResults = Collections.synchronizedList(new ArrayList<AsBs>(4));

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            partialResults.add(new AsBs());
        }
        barrier = new CyclicBarrier(4, new CheckerThread());

        System.out.println("Spawning 4 worker threads to compute partial results.");

        String[] initials = {"BACDBD", "ABAADCABC", "BDACADAC", "D"};
        for (int i = 0; i < 4; i++) {
            Thread worker = new Thread(new StringChangerThread(initials[i], i));
            worker.setName("Thread " + i);
            worker.start();
        }
    }
}
