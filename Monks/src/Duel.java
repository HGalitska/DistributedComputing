import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class Duel extends RecursiveTask<Monk> {
    ArrayList<Monk> opponents;

    Duel(ArrayList<Monk> monks) {
        opponents = monks;
    }

    @Override
    protected Monk compute() {
        if (opponents.size() == 1){
            return opponents.get(0);
        }

        Duel rightHalf = new Duel(new ArrayList<>(opponents.subList(0, opponents.size() / 2)));
        Duel leftHalf = new Duel(new ArrayList<>(opponents.subList(opponents.size() / 2, opponents.size())));

        rightHalf.fork();
        leftHalf.fork();

        Monk rightWinner = rightHalf.join();
        Monk leftWinner = leftHalf.join();

        if (rightWinner.compareTo(leftWinner) >= 0) return rightWinner;
        else return leftWinner;
    }
}
