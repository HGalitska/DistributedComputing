import java.util.ArrayList;

public class HoneyPot {
    private ArrayList<Integer> pot;
    private int size;

    HoneyPot(int size) {
        pot = new ArrayList<>();
        this.size = size;
    }

    void put(int sip) {
        if (!isFull()) {
            pot.add(sip);
        }
    }

    void eatWhole() {
        pot.clear();
    }

    boolean isEmpty() {
        return pot.size() == 0;
    }

    boolean isFull() {
        return pot.size() == size;
    }

    int getSize() {
        return pot.size();
    }
}
