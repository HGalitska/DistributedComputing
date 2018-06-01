import dao.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class XMLLoader {

    final static String XML_FILE = "file.xml";
    final static String XSD_FILE = "department.xsd";

    //-----------------------------------------------------------------

    //TODO: saving from Form to XML file
    //TODO: attributes to elements
    public static void loadFromFile() {
        Document doc = null;

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema s = sf.newSchema(new File(XSD_FILE));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setSchema(s);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new SimpleErrorHandler());
            doc = db.parse(new File(XML_FILE));
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
                    GroupDao groupDao = new GroupDaoImpl();
                    groupDao.addGroup(new Group(groupCode, groupName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NodeList listStudents = group.getElementsByTagName("student");

                for (int j = 0; j < listStudents.getLength(); j++) {
                    Element student = (Element) listStudents.item(j);
                    int studentCode = Integer.parseInt(student.getAttribute("id"));
                    String studentName = student.getAttribute("name");
                    try {
                        StudentDao studentDao = new StudentDaoImpl();
                        studentDao.addStudent(new Student(studentCode, studentName, groupCode));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
