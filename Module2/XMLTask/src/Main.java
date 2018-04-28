public class Main {
    public static void main(String[] args) {
        StudiesDepartment studiesDepartment = new StudiesDepartment();
        studiesDepartment.loadFromFile("xmls/input.xml");
        try {
            studiesDepartment.addGroup(4, "МСС 3");
            studiesDepartment.addStudent(8, "Ваня Яковлев", false, 4);

            studiesDepartment.printGroups();
            studiesDepartment.printStudents();

            studiesDepartment.deleteStudent(2);
            studiesDepartment.deleteGroup(1);

            studiesDepartment.printStudentsForGroup(3);

        } catch (Exception e) {
            e.printStackTrace();
        }

        studiesDepartment.saveToFile("xmls/output.xml");
    }
}
