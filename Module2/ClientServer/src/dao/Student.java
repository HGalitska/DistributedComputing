package dao;

public class Student {
    public int      code;
    public String   name;
    public int      groupId;

    public Student() {
        code = -1;
        name = null;
        groupId = -1;
    }

    public Student(int code, String name, int group){
        this.code = code;
        this.name = name;
        this.groupId = group;
    }

    //********************************************************************************//

}
