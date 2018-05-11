import java.sql.*;
import java.util.ArrayList;
import com.sun.xml.internal.ws.util.pipe.DumpTube;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StudiesDepartment {

    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();

    final String XML_FILE = "file.xml";

    Connection connection;

    StudiesDepartment() {
        loadFromDB();
    }
    //-----------------------------------------------------------------

    private void getFile(Document doc, String filename) {
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(filename));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.ENCODING, "WINDOWS-1251");
        try {
            transformer.transform(domSource, fileResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        Document doc = null;

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema s = sf.newSchema(new File("department.xsd"));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setSchema(s);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new SimpleErrorHandler());
            doc = db.parse(new File(filename));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        Element root = doc.getDocumentElement();

        if (root.getTagName().equals("department")) {
            NodeList listGroups = root.getElementsByTagName("group");

            for (int i = 0; i < listGroups.getLength(); i++) {

                Element group = (Element) listGroups.item(i);
                int groupCode = Integer.parseInt(group.getAttribute("id"));
                String groupName = group.getAttribute("name");
                try {
                    addGroup(groupCode, groupName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NodeList listStudents = group.getElementsByTagName("student");

                for (int j = 0; j < listStudents.getLength(); j++) {
                    Element student = (Element) listStudents.item(j);
                    int studentCode = Integer.parseInt(student.getAttribute("id"));
                    String studentName = student.getAttribute("name");
                    boolean isCaptain = Integer.parseInt(student.getAttribute("iscap")) != 0;
                    try {
                        addStudent(studentCode, studentName, isCaptain, groupCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------

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

    public Group getGroup(int code)  {
        for (Group group : groups) {
            if (group.code == code) {
                return group;
            }
        }

        //throw new Exception("Group with this code does not exists.");
        return null;
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

    public Student getStudent(int code)  {
        for (Student student : students) {
            if (student.code == code) {
                return student;
            }
        }

        //throw new Exception("Student with this code does not exists.");
        return null;
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
