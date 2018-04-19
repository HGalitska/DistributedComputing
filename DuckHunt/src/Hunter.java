import java.util.ArrayList;
import java.util.List;

public class Hunter {
    Position position;
    List<Shot> shots;

    Hunter(int x, int y) {
        this.position = new Position(x, y);

        shots = new ArrayList<>();
        ///////////
    }

}
