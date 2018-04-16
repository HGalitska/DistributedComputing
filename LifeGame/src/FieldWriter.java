import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldWriter implements Runnable {
    ReentrantReadWriteLock lock;
    volatile Field field;

    FieldGrid fieldGrid;

    FieldWriter(Field field, FieldGrid fieldGrid, ReentrantReadWriteLock lock){
        this.field = field;
        this.fieldGrid = fieldGrid;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.writeLock().lock();
        field.updateField();
        lock.writeLock().unlock();
        fieldGrid.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
