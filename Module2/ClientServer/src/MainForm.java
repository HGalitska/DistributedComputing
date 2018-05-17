import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;

public class MainForm extends JFrame {

    private StudiesDepartment department;
    private JTable studentsTable;
    private JTable groupsTable;

    private JTextPane resultTextPane;

    private JTextField studentCodeField1;
    private JTextField studentNameField;
    private JTextField isCapField;
    private JTextField groupCodeField1;
    private JTextField groupCodeField2;
    private JTextField groupNameField;
    private JTextField studentCodeField2;
    private JTextField groupCodeField3;

    private MainForm() {
        setLayout(null);

        department = new StudiesDepartment();

        initTables();

        initResultPane();
        initFields();
        initButtons();

        setSize(500, 620);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //--------------------------------------------------------------------------------------------

    private void addStudentRow(Student s){
        DefaultTableModel studentsModel = (DefaultTableModel) studentsTable.getModel();
        Object[] rowData = new Object[4];
        rowData[0] = s.code;
        rowData[1] = s.name;
        rowData[2] = s.isCaptain;
        rowData[3] = s.group.code;
        studentsModel.addRow(rowData);
        studentsTable.updateUI();
    }

    private void addGroupRow(Group g){
        DefaultTableModel groupsModel = (DefaultTableModel) groupsTable.getModel();
        Object[] rowData = new Object[2];
        rowData[0] = g.code;
        rowData[1] = g.name;
        groupsModel.addRow(rowData);
        groupsTable.updateUI();
    }

    private void initTables() {
        initStudentsTable();
        initGroupsTable();

        JScrollPane studentsScrollPane = new JScrollPane(studentsTable);
        JScrollPane groupsScrollPane = new JScrollPane(groupsTable);

        JTabbedPane pane = new JTabbedPane();
        pane.setBounds(0, 0, 500, 300);
        pane.add("Students", studentsScrollPane);
        pane.add("Groups", groupsScrollPane);

        add(pane);
    }

    private void initStudentsTable() {
        String[] studentsColumns = {"code", "name", "is_captain", "group_code"};
        Object[] rowData = new Object[4];
        DefaultTableModel studentsModel = new DefaultTableModel();
        studentsModel.setColumnIdentifiers(studentsColumns);
        for (int i = 0; i < department.students.size(); i++) {
            rowData[0] = department.students.get(i).code;
            rowData[1] = department.students.get(i).name;
            rowData[2] = department.students.get(i).isCaptain;
            rowData[3] = department.students.get(i).group.code;
            studentsModel.addRow(rowData);
        }
        studentsTable = new JTable(studentsModel);
        studentsTable.setBackground(new Color(246, 217, 221));

        studentsTable.setFillsViewportHeight(true);
        studentsTable.setColumnSelectionAllowed(false);
        studentsTable.setRowSelectionAllowed(true);

        addStudentsTableListener();
    }

    //---------------------------------------------

    private void addStudentsTableListener() {
        studentsTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                studentsTable.getModel().removeTableModelListener(this);

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = studentsTable.getColumnName(column);

                    if (columnName.equals("name")) {
                        updateStudentName(row, column);
                    }

                    if (columnName.equals("is_captain")) {
                        updateStudentIsCap(row, column);
                    }

                    if (columnName.equals("group_code")) {
                        updateStudentGroup(row, column);
                    }
                }
                studentsTable.getModel().addTableModelListener(this);
            }
        });
    }

    private void updateStudentName(int row, int column) {
        String oldName = department.students.get(row).name;
        String newName = (String) studentsTable.getValueAt(row, column);

        if (!oldName.equals(newName)) {
            department.students.get(row).name = newName;
            resultTextPane.setText("Student's name has been changed: " + oldName + " -> " + newName);
        }
    }

    private void updateStudentIsCap(int row, int column) {
        Boolean oldIsCaptain = department.students.get(row).isCaptain;
        Boolean newIsCaptain = Boolean.parseBoolean((String) studentsTable.getValueAt(row, column));

        if (oldIsCaptain != newIsCaptain) {
            department.students.get(row).isCaptain = newIsCaptain;
            resultTextPane.setText("Student's status has been changed: " + oldIsCaptain + " -> " + newIsCaptain);
        }
    }

    private void updateStudentGroup(int row, int column) {
        int oldGroupCode = department.students.get(row).group.code;
        int newGroupCode = Integer.parseInt((String) studentsTable.getValueAt(row, column));

        if (oldGroupCode != newGroupCode) {
            try {
                department.students.get(row).group = department.getGroup(newGroupCode);
                resultTextPane.setText("Student's group has been changed: " + oldGroupCode + " -> " + newGroupCode);
            } catch (Exception ex) {
                resultTextPane.setText("!!!ATTENTION!!! This group doesn't exist!");
                studentsTable.setValueAt(oldGroupCode, row, column);
                studentsTable.updateUI();
            }
        }
    }

    //---------------------------------------------

    private void initGroupsTable() {
        String[] groupsColumns = {"code", "name"};
        Object[] rowData = new Object[2];
        DefaultTableModel groupsModel = new DefaultTableModel();
        groupsModel.setColumnIdentifiers(groupsColumns);
        for (int i = 0; i < department.groups.size(); i++) {
            rowData[0] = department.groups.get(i).code;
            rowData[1] = department.groups.get(i).name;
            groupsModel.addRow(rowData);
        }
        groupsTable = new JTable(groupsModel);
        groupsTable.setBackground(new Color(254, 221, 109));

        groupsTable.setFillsViewportHeight(true);
        groupsTable.setColumnSelectionAllowed(false);
        groupsTable.setRowSelectionAllowed(true);

        addGroupsTableListener();
    }

    private void addGroupsTableListener() {

        groupsTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                groupsTable.getModel().removeTableModelListener(this);

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = groupsTable.getColumnName(column);

                    if (columnName.equals("name")) {
                        updateGroupName(row, column);
                    }

                }
                groupsTable.getModel().addTableModelListener(this);
            }
        });
    }

    private void updateGroupName(int row, int column) {
        String oldName = department.groups.get(row).name;
        String newName = (String) groupsTable.getValueAt(row, column);

        if (!oldName.equals(newName)) {
            department.groups.get(row).name = newName;
            resultTextPane.setText("Group's name has been changed: " + oldName + " -> " + newName);
        }
    }

    //--------------------------------------------------------------------------------------------

    private void initResultPane() {
        resultTextPane = new JTextPane();
        resultTextPane.setText("Here you will see results. " +
                "\n\n Select row to delete it. " +
                "\n Double click on the cell to edit it.");
        resultTextPane.setBounds(5, 450, 490, 100);
        resultTextPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextPane);
        add(resultTextPane);
    }

    //---------------------------------------------

    private void initButtons() {

        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setBounds(400, 300, 90, 20);
        addStudentButton.addActionListener((e) -> addStudentButtonListener());

        add(addStudentButton);

        JButton addGroupButton = new JButton("Add Group");
        addGroupButton.setBounds(400, 330, 90, 20);
        addGroupButton.addActionListener((e) -> addGroupButtonListener());

        add(addGroupButton);

        // -------------------------------------------------------------

        JButton findStudentButton = new JButton("Find Student");
        findStudentButton.setBounds(110, 370, 90, 20);
        findStudentButton.addActionListener((e) -> findStudentButtonListener());

        add(findStudentButton);

        JButton findGroupButton = new JButton("Find Group");
        findGroupButton.setBounds(400, 370, 90, 20);
        findGroupButton.addActionListener((e) -> findGroupButtonListener());

        add(findGroupButton);

        JButton findStudentsInGroupButton = new JButton("Students in Group");
        findStudentsInGroupButton.setBounds(400, 400, 90, 20);
        findStudentsInGroupButton.addActionListener((e) -> findStudentsInGroupButtonListener());

        add(findStudentsInGroupButton);

        // -------------------------------------------------------------

        JButton deleteRowButton = new JButton("Delete Row");
        deleteRowButton.setBounds(5, 560, 90, 20);
        deleteRowButton.addActionListener((e) -> deleteRowButtonListener());

        add(deleteRowButton);

        JButton saveToFileButton = new JButton("Save");
        saveToFileButton.setBounds(400, 560, 90, 20);
        saveToFileButton.addActionListener((e) -> {
            department.saveToDB();
            resultTextPane.setText("Changes have been saved.");
        });

        add(saveToFileButton);

        JButton loadFileButton = new JButton("Load File");
        loadFileButton.setBounds(305, 560, 90, 20);
        loadFileButton.addActionListener((e) -> loadFileButtonListener());

        add(loadFileButton);
    }

    private void addStudentButtonListener() {
        if (studentCodeField1.getText().equals("")) {
            resultTextPane.setText("New student should have code!");
            return;
        }
        int code = Integer.parseInt(studentCodeField1.getText());
        for (Student s : department.students) {
            if (s.code == code) {
                resultTextPane.setText("Student with this code already exists.");
                return;
            }
        }

        String name = studentNameField.getText();
        Boolean isCap = Boolean.parseBoolean(isCapField.getText());

        int groupCode = Integer.parseInt(groupCodeField1.getText());
        try {
            Group group = department.getGroup(groupCode);
            department.addStudent(code, name, isCap, group.code);
        } catch (Exception e) {
            resultTextPane.setText("This group doesn't exist!");
            return;
        }

        Object[] rowData = new Object[4];
        rowData[0] = code;
        rowData[1] = name;
        rowData[2] = isCap;
        rowData[3] = groupCode;

        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
        model.addRow(rowData);
        studentsTable.updateUI();
        resultTextPane.setText("New student was added!");
    }

    private void addGroupButtonListener() {
        if (groupCodeField2.getText().equals("")) {
            resultTextPane.setText("New group should have code!");
            return;
        }
        int code = Integer.parseInt(groupCodeField2.getText());

        String name = groupNameField.getText();
        try {
            department.addGroup(code, name);
        } catch (Exception e) {
            resultTextPane.setText("Group with this code already exists.");
        }

        Object[] rowData = new Object[4];
        rowData[0] = code;
        rowData[1] = name;

        DefaultTableModel model = (DefaultTableModel) groupsTable.getModel();
        model.addRow(rowData);
        groupsTable.updateUI();
        resultTextPane.setText("New group was added!");
    }

    private void findStudentButtonListener() {
        if (studentCodeField2.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a student!");
            return;
        }
        int code = Integer.parseInt(studentCodeField2.getText());

        try {
            Student s = department.getStudent(code);
            resultTextPane.setText("Name: " + s.name + "\nIs captain: "
                    + s.isCaptain + "\nGroup: " + s.group.name);
        } catch (Exception e) {
            resultTextPane.setText("Student with this code doesn't exist!");
        }
    }

    private void findGroupButtonListener() {
        if (groupCodeField3.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a group!");
            return;
        }
        int code = Integer.parseInt(groupCodeField3.getText());

        try {
            Group g = department.getGroup(code);
            resultTextPane.setText("Group name: " + g.name);
        } catch (Exception e) {
            resultTextPane.setText("Group with this code doesn't exist!");
        }
    }

    private void findStudentsInGroupButtonListener() {
        if (groupCodeField3.getText().equals("")) {
            resultTextPane.setText("Enter code to search for a group!");
            return;
        }
        int code = Integer.parseInt(groupCodeField3.getText());

        try {
            Group g = department.getGroup(code);
            resultTextPane.setText("Group name: " + g.name);

            ArrayList<Student> students = department.printStudentsForGroup(code);
            resultTextPane.setText("");
            for (Student s : students) {
                resultTextPane.setText(resultTextPane.getText() + "\n Name: " + s.name);
            }
        } catch (Exception e) {
            resultTextPane.setText("Group with this code doesn't exist!");
        }
    }

    private void deleteRowButtonListener() {
        DefaultTableModel studentModel = (DefaultTableModel) studentsTable.getModel();
        int selRow = studentsTable.getSelectedRow();
        if (selRow != -1) {
            try {
                department.deleteStudent((Integer) studentsTable.getValueAt(selRow, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            studentModel.removeRow(selRow);
            studentsTable.updateUI();
            resultTextPane.setText("Student was deleted.");
        }

        DefaultTableModel groupModel = (DefaultTableModel) groupsTable.getModel();
        selRow = groupsTable.getSelectedRow();
        if (selRow != -1) {
            try {
                department.deleteGroup((Integer) groupsTable.getValueAt(selRow, 0));

            } catch (Exception e) {
                e.printStackTrace();
            }


            groupModel.removeRow(selRow);
            groupsTable.updateUI();
            resultTextPane.setText("Group was deleted.");
        }

    }

    private void loadFileButtonListener() {
        int oldGroupsCount = department.groups.size();
        int oldStudentsCount = department.students.size();
        ArrayList<Student> oldStudents = department.students;
        ArrayList<Group> oldGroups = department.groups;
        department.loadFromFile(department.XML_FILE);
        int numberOfNewGroups = department.groups.size() - oldGroupsCount;
        int numberOfNewStudents = department.students.size() - oldStudentsCount;

        for (int i = oldStudentsCount; i < department.students.size(); i++) {
            //if (department.getStudent(oldStudents.get(i).code) == null)
                addStudentRow(department.students.get(i));
//            else {
//                JDialog d = new JDialog(this, "Attention!", true);
//                d.setLayout( new FlowLayout() );
//                JButton b1 = new JButton ("Skip");
//                JButton b2 = new JButton ("Sk");
//                JButton b3 = new JButton ("Replace");
//                b.addActionListener ( new ActionListener()
//                {
//                    public void actionPerformed( ActionEvent e )
//                    {
//                        DialogExample.d.setVisible(false);
//                    }
//                });
//                d.add( new JLabel ("Click button to continue."));
//                d.add(b);
//                d.setSize(300,300);
//                d.setVisible(true);
//
//            }
        }

        for (int i = oldGroupsCount; i < department.groups.size(); i++) {
            //if (department.getGroup(oldGroups.get(i).code) == null)
                addGroupRow(department.groups.get(i));
//            else {
//
//            }

        }


    }

    //---------------------------------------------

    private void initFields() {
        studentCodeField1 = new JTextField();
        studentCodeField1.setBounds(5, 300, 90, 20);
        studentCodeField1.setToolTipText("student code");
        add(studentCodeField1);

        studentNameField = new JTextField();
        studentNameField.setBounds(95, 300, 120, 20);
        studentNameField.setToolTipText("student name");
        add(studentNameField);

        isCapField = new JTextField();
        isCapField.setBounds(215, 300, 90, 20);
        isCapField.setToolTipText("is student a captain?");
        add(isCapField);

        groupCodeField1 = new JTextField();
        groupCodeField1.setBounds(305, 300, 90, 20);
        groupCodeField1.setToolTipText("group code");
        add(groupCodeField1);

        //----------------------------------------------

        groupCodeField2 = new JTextField();
        groupCodeField2.setBounds(215, 330, 90, 20);
        groupCodeField2.setToolTipText("group code");
        add(groupCodeField2);

        groupNameField = new JTextField();
        groupNameField.setBounds(305, 330, 90, 20);
        groupNameField.setToolTipText("group name");
        add(groupNameField);

        //----------------------------------------------

        studentCodeField2 = new JTextField();
        studentCodeField2.setBounds(5, 370, 90, 20);
        studentCodeField2.setToolTipText("student code");
        add(studentCodeField2);

        groupCodeField3 = new JTextField();
        groupCodeField3.setBounds(305, 370, 90, 20);
        groupCodeField3.setToolTipText("group code");
        add(groupCodeField3);
    }

    //--------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        new MainForm();
    }
}
