public class Monk implements Comparable<Monk>{
    String name;
    int age;
    int energy;
    boolean yan;

    Monk(String n, int a, int en, boolean y){
        name = n;
        age = a;
        energy = en;
        yan = y;
    }

    @Override
    public int compareTo(Monk other) {
        if (this.energy == other.energy) return 0;
        if (this.energy > other.energy) return 1;
        return  -1;
    }
}
