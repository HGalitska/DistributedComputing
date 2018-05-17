import dao.Group;
import dao.GroupDao;
import dao.GroupDaoImpl;

public class Main {
    public static void main(String[] args) {
        GroupDao groupDao = new GroupDaoImpl();
        Group group = groupDao.getGroup(3);
        System.out.println(group.code);
        System.out.println(group.name);

        groupDao.deleteGroup(5);

    }
}
