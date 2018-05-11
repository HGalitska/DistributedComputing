import java.sql.*;
import java.util.ArrayList;

public class StudiesDepartment {

    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();

    Connection connection;

    StudiesDepartment() {
        loadFromDB();
    }

    public void saveToDB() {
        try {
            Statement s = connection.createStatement();
            String sql = "SELECT COUNT(1) FROM studydep.GROUPS";
            ResultSet rs = s.executeQuery(sql);
            rs.next();
            int countRows = rs.getInt(1);
            rs.close();

            while (countRows < groups.size()) {
                sql = "INSERT INTO studydep.GROUPS VALUES (?, '')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, groups.get(countRows).code);
                preparedStatement.executeUpdate();

                countRows++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Group g : groups) {
            try {
                String sql = "UPDATE studydep.GROUPS SET GROUPS.NAME = ? WHERE CODE = ? or CODE = 0";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, g.name);
                preparedStatement.setInt(2, g.code);
                //preparedStatement.setInt(3, g.code);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Statement s = connection.createStatement();
            String sql = "SELECT COUNT(1) FROM studydep.STUDENTS";
            ResultSet rs = s.executeQuery(sql);
            rs.next();
            int countRows = rs.getInt(1);
            rs.close();

            while (countRows < students.size()) {
                sql = "INSERT INTO studydep.STUDENTS VALUES (?, '', 0, 0)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, students.get(countRows).code);
                preparedStatement.executeUpdate();

                countRows++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Student s : students) {
            try {
                String sql = "UPDATE studydep.STUDENTS SET STUDENTS.NAME = ? , " +
                        "STUDENTS.ISCAPTAIN = ?, STUDENTS.GROUPCODE = ? WHERE CODE = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, s.name);
                if (s.isCaptain) preparedStatement.setInt(2, 1);
                else preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, s.group.code);
                preparedStatement.setInt(4, s.code);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFromDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studydep?useSSL=FALSE" +
                    "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false" +
                    "&serverTimezone=UTC", "root", "root2397");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement s = connection.createStatement();
            String sql = "SELECT * FROM studydep.groups";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("CODE");
                String name = rs.getString("NAME");
                addGroup(code, name);
            }
            rs.close();

            sql = "SELECT * FROM studydep.students";
            rs = s.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("CODE");
                String name = rs.getString("NAME");
                int isCap = rs.getInt("isCaptain");
                int groupCode = rs.getInt("groupCode");
                addStudent(code, name, (isCap == 1), groupCode);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //********************************************************************************//
    public void addGroup(int code, String name) throws Exception {
        for (Group group : groups) {
            if (group.code == code) {
                throw new Exception("Group with this code already exists.");
            }
        }

        groups.add(new Group(code, name));
    }

    public Group getGroup(int code) throws Exception {
        for (Group group : groups) {
            if (group.code == code) {
                return group;
            }
        }

        throw new Exception("Group with this code does not exists.");
    }

    public Group getGroupInd(int index) throws Exception {
        if (index < 0 || index > groups.size() - 1) {
            throw new Exception("Group index is out of bounds");
        }

        return groups.get(index);
    }

    public int countGroups() {
        return groups.size();
    }

    public void deleteGroup(int code) throws Exception {

        Group group = getGroup(code);

        for (Student student : students) {
            if (student.group == group) {
                students.remove(student);
            }
        }

        groups.remove(group);

        String sql = "DELETE FROM studydep.GROUPS WHERE CODE = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, group.code);
        preparedStatement.executeUpdate();

    }

    public ArrayList<Student> printStudentsForGroup(int code) throws Exception {
        Group group = getGroup(code);
        ArrayList<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.group.code == group.code) {
                result.add(student);
            }
        }

        return result;
    }

    //********************************************************************************//

    public void addStudent(int code, String name, boolean isCaptain, int groupCode) throws Exception {
        for (Student student : students) {
            if (student.code == code) {
                throw new Exception("Student with this code already exists.");
            }
        }

        Group group = getGroup(groupCode);

        students.add(new Student(code, name, group, isCaptain));
    }

    public Student getStudent(int code) throws Exception {
        for (Student student : students) {
            if (student.code == code) {
                return student;
            }
        }

        throw new Exception("Student with this code does not exists.");
    }

    public Student getStudentInd(int index) throws Exception {
        if (index < 0 || index > students.size() - 1) {
            throw new Exception("Student index is out of bounds");
        }

        return students.get(index);
    }

    public int countStudents() {
        return students.size();
    }

    public void deleteStudent(int code) throws Exception {
        for (Student student : students) {
            if (student.code == code) {
                students.remove(student);

                String sql = "DELETE FROM studydep.STUDENTS WHERE CODE = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, student.code);
                preparedStatement.executeUpdate();

                return;
            }
        }

        throw new Exception("Student with this code does not exist.");
    }

    //********************************************************************************//

    public void printGroups() {
        System.out.println("All groups:");
        for (Group group : groups) {
            System.out.println(group.code + " " + group.name);
        }
    }

    public void printStudents() {
        System.out.println("All student:");
        for (Student student : students) {
            if (student.isCaptain)
                System.out.println(student.code + " *" + student.name);
            else
                System.out.println(student.code + "  " + student.name);
        }
    }
}
