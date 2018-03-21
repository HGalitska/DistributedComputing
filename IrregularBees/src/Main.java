import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    volatile static boolean foundBear = false;

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Number of sectors: ");
        int sectorsNumber = reader.nextInt();

        System.out.println("Number of cells in 1 sector: ");
        int cellsInSector = reader.nextInt();

        reader.close();

        ArrayList<ArrayList<Boolean>> forest = new ArrayList<>();
        for (int i = 0; i < sectorsNumber; i++) {
            ArrayList<Boolean> newSector = new ArrayList<>();
            for (int j = 0; j < cellsInSector; j++) {
                newSector.add(false);
            }
            forest.add(newSector);
        }

        Random rand = new Random();

        int sectorIndex = rand.nextInt(sectorsNumber);
        int cellIndex = rand.nextInt(cellsInSector);

        System.out.println("TOP SECRET: Winnie is in " + sectorIndex + " sector, " + cellIndex + " cell.");

        forest.get(sectorIndex).set(cellIndex, true);

        ArrayList<Flock> flocks = new ArrayList<>();

        for (int i = 0; i < sectorsNumber; i++) {
            Flock newFlock = new Flock(i, forest.get(i));
            flocks.add(newFlock);
        }

        int i = 0;
        while (i < sectorsNumber / 2 + 1) {
            flocks.get(i).start();
            i++;
        }

        while (i >= 0) {
            try {
                flocks.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;
        }

        if (!foundBear) {
            System.out.println("------------------------------------------------------------------------------------------");
            i = sectorsNumber / 2 + 1;
            while (i < sectorsNumber) {
                flocks.get(i).start();
                i++;
            }
        }

    }
}
