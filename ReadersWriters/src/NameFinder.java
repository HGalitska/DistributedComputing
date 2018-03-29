import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

public class NameFinder extends Thread {
    int id;
    ReadWriteLock lock;
    File file;
    String[] namesToSearch;

    NameFinder(ReadWriteLock lock, File file, String[] namesToSearch, int id) {
        this.id = id;
        this.lock = lock;
        this.file = file;
        this.namesToSearch = namesToSearch;
    }

    String getNumber(String name) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] res = line.split(" ");
                String foundName = res[0] + " " + res[1];
                String foundNumber = res[2];
                if (foundName.equals(name)) return foundNumber;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void run() {
        lock.readLock().lock();
        for(String name : namesToSearch) {
            String phone = getNumber(name);
            System.out.println("Name Finder" + id + "  found phone for name " + name + " : " + phone);
        }
        lock.readLock().unlock();
    }
}
