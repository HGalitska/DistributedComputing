import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    volatile static boolean foundBear = false;
    volatile static ArrayList<Boolean> searchedSectors = new ArrayList<>();
    static ArrayList<ArrayList<Boolean>> forest = new ArrayList<>();

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Number of sectors: ");
        int sectorsNumber = reader.nextInt();

        System.out.println("Number of cells in 1 sector: ");
        int cellsInSector = reader.nextInt();

        reader.close();

        for (int i = 0; i < sectorsNumber; i++) {
            ArrayList<Boolean> newSector = new ArrayList<>();
            for (int j = 0; j < cellsInSector; j++) {
                newSector.add(false);
            }
            forest.add(newSector);
            searchedSectors.add(false);
        }

        Random rand = new Random();

        int sectorIndex = rand.nextInt(sectorsNumber);
        int cellIndex = rand.nextInt(cellsInSector);

        System.out.println("TOP SECRET: Winnie is in " + sectorIndex + " sector, " + cellIndex + " cell.");

        forest.get(sectorIndex).set(cellIndex, true);

        for (int i = 0; i < 3; i++) {
            Flock newFlock = new Flock(i);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            newFlock.start();
        }

    }
}
