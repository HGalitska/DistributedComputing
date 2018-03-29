import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

public class PhoneFinder extends Thread {
    int id;
    ReadWriteLock lock;
    File file;
    String[] phonesToSearch;

    PhoneFinder(ReadWriteLock lock, File file, String[] phonesToSearch, int id) {
        this.id = id;
        this.lock = lock;
        this.file = file;
        this.phonesToSearch = phonesToSearch;
    }

    String getName(String phoneNumber) {
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
                String foundNumber = res[2];
                String foundName = res[0] + " " + res[1];
                if (foundNumber.equals(phoneNumber)) return foundName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void run() {
        lock.readLock().lock();
        for(String phone : phonesToSearch) {
            String name = getName(phone);
            System.out.println("Phone Finder " + id + " found name for number " + phone + " : " + name);
        }
        lock.readLock().unlock();
    }
}
