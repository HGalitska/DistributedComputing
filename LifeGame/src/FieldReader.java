import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldReader extends Thread {
    private volatile Field field;

    private ReentrantReadWriteLock lock;
    private CyclicBarrier barrier;

    private int startAt;
    private int finishAt;
    private int numberOfGangs;

    FieldReader(Field field, CyclicBarrier barrier, ReentrantReadWriteLock lock, int startAt, int finishAt, int numberOfGangs) {
        this.field = field;
        this.barrier = barrier;
        this.lock = lock;
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.numberOfGangs = numberOfGangs;
    }

    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) break;

            lock.readLock().lock();
            field.simulate(numberOfGangs, startAt, finishAt);
            lock.readLock().unlock();

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
