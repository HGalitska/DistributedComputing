import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {

        ReadWriteLock lock = new ReentrantReadWriteLock();
        File file = new File("/Users/galitska/Developer/DistrComp/ReadersWriters/src/database.txt");

        String[] phonesToSearch = {"7734476", "3807671", "2598026", "3539372"};
        String[] namesToSearch = {"Carl Febus", "Georgina Degenhardt", "Donnetta Entwistle"};
        String[] linesToAdd1 = {"Hassie Aguilar 2722006", "Porter Gonzales 4688704", "Dominque Sampley 9801213",
                "Donn Kohl  8343770", "Aaron Belnap 7652741"};
        String[] linesToAdd2 = {"Markus Zabriskie 38905738", "Lucinda Wendt 1839295", "Helene Bialaszewski 3094005",
                "Jessika Duwe 1095893", "Claretta Brian 8700193"};

        PhoneFinder phoneFinder = new PhoneFinder(lock, file, phonesToSearch, 1);
        NameFinder nameFinder = new NameFinder(lock, file, namesToSearch, 2);
        Writer writer1 = new Writer(lock, file, linesToAdd1, 3);
        Writer writer2 = new Writer(lock, file, linesToAdd2, 4);

        phoneFinder.start();
        nameFinder.start();
        writer1.start();
        writer2.start();
    }
}
