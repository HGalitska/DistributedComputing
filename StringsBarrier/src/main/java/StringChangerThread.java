import com.google.common.base.CharMatcher;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class StringChangerThread implements Runnable {
    private char[] string;
    AsBs toCheck = new AsBs();
    int id;

    StringChangerThread(String string, int id) {
        this.id = id;
        this.string = string.toCharArray();

        recount();
    }

    void recount(){
        toCheck.numA = CharMatcher.is('A').countIn(new String(this.string));
        toCheck.numB = CharMatcher.is('B').countIn(new String(this.string));
    }

    void changeLetter(int index) {
        char randomLetter = string[index];

        switch (randomLetter) {
            case 'A':
                string[index] = 'C';
                recount();
                break;
            case 'B':
                string[index] = 'D';
                recount();
                break;
            case 'C':
                string[index] = 'A';
                recount();
                break;
            case 'D':
                string[index] = 'B';
                recount();
                break;
            default:
                System.out.println("Error.");
        }
    }

    public void run() {
        String thisThreadName = Thread.currentThread().getName();

        boolean match = false;
        while (!match) {
            int idx = new Random().nextInt(string.length);
            changeLetter(idx);

            System.out.println(thisThreadName
                    + ": Crunching some letters! Final result - " + new String(string));
            Main.partialResults.set(id, toCheck);

            int A_0 = Main.partialResults.get(0).numA;
            int B_0 = Main.partialResults.get(0).numB;
            int A_1 = Main.partialResults.get(1).numA;
            int B_1 = Main.partialResults.get(1).numB;
            int A_2 = Main.partialResults.get(2).numA;
            int B_2 = Main.partialResults.get(2).numB;
            int A_3 = Main.partialResults.get(3).numA;
            int B_3 = Main.partialResults.get(3).numB;
            System.out.println(A_0 + " " + A_1 + " " + A_2 + " " + A_3 + " " + B_0 + " " + B_1 + " " + B_2 + " " + B_3) ;

            if (B_0 == B_1 && B_1 == B_3 && A_0 == A_1 && A_1 == A_3) match = true;
            if (B_0 == B_1 && B_1 == B_2 && A_0 == A_1 && A_1 == A_2) match = true;
            if (B_0 == B_2 && B_2 == B_3 && A_0 == A_2 && A_2 == A_3) match = true;
            if (B_2 == B_1 && B_1 == B_3 && A_2 == A_1 && A_1 == A_3) match = true;


            System.out.println(match);
        }
        try {
            System.out.println(thisThreadName
                    + " waiting for others to reach barrier.");
            Main.barrier.await();
        } catch (InterruptedException e) {
            // ...
        } catch (BrokenBarrierException e) {
            // ...
        }
    }
}
