package dao;

import java.io.Serializable;

public class Group implements Serializable{
    public int      code;
    public String   name;

    public Group() {
        code = -1;
        name = null;
    }

    public Group(int code, String name) {
        this.code = code;
        this.name = name;
    }

    //********************************************************************************//


}
