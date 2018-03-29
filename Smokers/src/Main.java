import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Main {
    final static int TABACO = 0;
    final static int MATCHES = 1;
    final static int PAPER = 2;

    static Mediator Jocker;

    public static void main(String[] args) {
        Semaphore s = new Semaphore(1);

        ArrayList<Boolean> table = new ArrayList<>(Arrays.asList(false, false, false));
        Jocker = new Mediator("Jocker", table, s);
        Smoker Smoker0 = new Smoker("Smoker 0", TABACO, table, s);
        Smoker Smoker1 = new Smoker("Smoker 1", MATCHES, table, s);
        Smoker Smoker2 = new Smoker("Smoker 2", PAPER, table, s);

        Smoker0.start();
        Smoker1.start();
        Smoker2.start();

        Jocker.start();

    }
}