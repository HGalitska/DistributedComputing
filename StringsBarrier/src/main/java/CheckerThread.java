import java.util.List;

class CheckerThread implements Runnable {

    public void run() {

        String thisThreadName = Thread.currentThread().getName();

        List<AsBs> results = Main.partialResults;
        for(AsBs result: results) {
            System.out.println(thisThreadName + ": Final result A = " + result.numA + ", B = " + result.numB);
        }
    }
}