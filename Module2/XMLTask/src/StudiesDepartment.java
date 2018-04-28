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

    private ArrayList<Group> groups = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();

    public void saveToFile(String filename) {
        Document doc = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Element root = doc.createElement("department");
        doc.appendChild(root);

        for (Group group : groups) {
            Element groupTag = doc.createElement("group");
            groupTag.setAttribute("id", Integer.toString(group.code));
            groupTag.setAttribute("name", group.name);
            root.appendChild(groupTag);

            for (Student student : students) {
                if (student.group == group) {
                    Element studentTag = doc.createElement("student");
                    studentTag.setAttribute("id", Integer.toString(student.code));
                    studentTag.setAttribute("name", student.name);
                    if (student.isCaptain) studentTag.setAttribute("iscap", Integer.toString(1));
                    else studentTag.setAttribute("iscap", Integer.toString(0));

                    groupTag.appendChild(studentTag);
                }
            }
        }

        getFile(doc, filename);

    }

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
            Schema s = sf.newSchema(new File("xsd/department.xsd"));

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
    }

    public void printStudentsForGroup(int code) throws Exception {

        Group group = getGroup(code);

        for (Student student : students) {
            if (student.group.code == group.code) {
                if (student.isCaptain)
                    System.out.println(student.code + " *" + student.name);
                else
                    System.out.println(student.code + "  " + student.name);
            }
        }

        throw new Exception("Group with this code does not exist.");
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
