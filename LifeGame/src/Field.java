import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Field {
    private ReentrantReadWriteLock lock = null;

    int width;
    int height;

    private Cell[][] currentState = null;
    private Cell[][] nextState = null;

    Field(int width, int height, ReentrantReadWriteLock lock) {
        this.width = width;
        this.height = height;
        this.lock = lock;

        currentState = new Cell[width][height];
        nextState = new Cell[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                currentState[i][j] = new Cell();
                nextState[i][j] = new Cell();
            }
    }

    Cell getCell(int x, int y) {
        return currentState[x][y];
    }

    void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentState[i][j].underGang = 0;
                nextState[i][j].underGang = 0;
            }
        }
    }

    void generate(int numberOfGangs) {
        Random rand = new Random();
        int cellsPerGang = (int) (width * height * 0.5/ numberOfGangs) ;
        for (int i = 1; i <= numberOfGangs; i++) {
            for (int j = 0; j < cellsPerGang; j++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);

                if (currentState[x][y].underGang == 0) {
                    currentState[x][y].underGang = i;
                }
            }
        }
    }

    void simulate(int numberOfGangs, int startAt, int finishAt) {
        for (int gang = 1; gang <= numberOfGangs; gang++) {
            for (int x = startAt; x < finishAt; x++) {
                for (int y = 0; y < width; y++) {
                    nextState[x][y].lock.writeLock().lock();
                    nextState[x][y].underGang = updateCell(currentState[x][y].underGang, nextState[x][y].underGang, livingNeighbours(x, y, gang), gang);
                    nextState[x][y].lock.writeLock().unlock();
                }
            }
        }
    }

    private int updateCell(int cell, int updated, int neighbours, int gang) {
        if (cell == gang) {
            // die because of loneliness
            if (neighbours < 2) return 0;

                // live
            else if (neighbours == 2 || neighbours == 3) return gang;

                // die beacause of overfilled
            else return 0;
        } else if (neighbours == 3) return gang;

        return updated;
    }

    private int livingNeighbours(int x, int y, int gang) {
        int result = 0;

        int[][] neighbourMask = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

        for (int i = 0; i < 8; i++) {
            int neighbourX = x + neighbourMask[i][0];
            int neighbourY = y + neighbourMask[i][1];
            if (isInBounds(neighbourX, neighbourY)) {
                if (currentState[neighbourX][neighbourY].underGang == gang)
                    result++;
            }
        }
        return result;
    }

    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < height && y >= 0 && y < width);
    }

    void updateField() {
        currentState = nextState;
        nextState = new Cell[height][width];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++) {
                nextState[i][j] = new Cell();
            }
    }
}
