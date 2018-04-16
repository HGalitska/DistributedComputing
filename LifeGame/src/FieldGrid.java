import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldGrid extends JPanel {
    final static int NUMBER_OF_READERS = 5;
    private volatile Field field;

    private ReentrantReadWriteLock lock;
    private CyclicBarrier barrier;

    private int cellSize;
    private int cellGap = 1;

    FieldReader[] readers = null;
    private FieldWriter writer;

    private final Color[] gangs = {Color.WHITE, new Color(192, 197, 255), new Color(238, 90, 126), new Color(181, 229, 218)};

    FieldGrid(int width, int height, int cellSize) {
        field = new Field(width, height, lock);
        this.cellSize = cellSize;
        lock = new ReentrantReadWriteLock();
    }

    void startLife(int numberOfGangs) {
        readers = null;
        writer = new FieldWriter(field, this, lock);
        barrier = new CyclicBarrier(NUMBER_OF_READERS, writer);

        field.clear();
        field.generate(numberOfGangs);
        int readerBlockSize = field.height / NUMBER_OF_READERS;
        readers = new FieldReader[NUMBER_OF_READERS];
        for (int i = 0; i < NUMBER_OF_READERS; i++) {
            readers[i] = new FieldReader(field, barrier, lock, readerBlockSize * i, readerBlockSize * (i + 1), numberOfGangs);
        }
        for (int i = 0; i < NUMBER_OF_READERS; i++) readers[i].start();
    }

    void stopLife() {
        if (readers != null)
            for (int i = 0; i < readers.length; i++) {
                readers[i].interrupt();
            }
        readers = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (field != null) {
            lock.readLock().lock();
            super.paintComponent(g);
            Insets b = getInsets();
            for (int y = 0; y < field.height; y++) {
                for (int x = 0; x < field.width; x++) {
                    int cell = field.getCell(x, y).underGang;
                    g.setColor(gangs[cell]);
                    g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                            * (cellSize + cellGap), cellSize, cellSize);
                }
            }
            lock.readLock().unlock();
        }
    }
}
