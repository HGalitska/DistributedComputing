import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

public class Writer extends Thread {
    int id;
    ReadWriteLock lock;
    File file;
    String[] linesToAdd;

    Writer(ReadWriteLock lock, File file, String[] linesToAdd, int id) {
        this.id = id;
        this.lock = lock;
        this.file = file;
        this.linesToAdd = linesToAdd;
    }

    void addLine(String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        lock.writeLock().lock();
        for(String line : linesToAdd) {
            addLine(line);
            System.out.println("Writer " + id + " added line: " + line);
        }
        lock.writeLock().unlock();
    }
}
