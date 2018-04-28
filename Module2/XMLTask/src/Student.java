public class Student {
    public int      code;
    public String   name;
    public Group    group;

    public boolean  isCaptain;

    public Student() {
        code = -1;
        name = null;
        group = null;
        isCaptain = false;
    }

    public Student(int code, String name, Group group, boolean isCaptain){
        this.code = code;
        this.name = name;
        this.group = group;

        this.isCaptain = isCaptain;
    }

    //********************************************************************************//

}
