import java.awt.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;

public class MainForm extends JFrame {

    private StudiesDepartment department;
    private JTable studentsTable;
    private JTable groupsTable;

    private JTextPane resultTextPane;

    private JButton addStudentButton;
    private JButton addGroupButton;
    private JButton findStudentButton;
    private JButton findGroupButton;
    private JButton findStudentsInGroupButton;
    private JButton deleteRowButton;
    private JButton saveToFileButton;

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
        initButtons();
        initFields();

        setSize(500, 620);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //----------------------------------------------

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

    private void addStudentsTableListener() {
        studentsTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                studentsTable.getModel().removeTableModelListener(this);
                int row = e.getFirstRow();
                int column = e.getColumn();
                String oldName = "2";

                int codeColumn = studentsTable.getColumnCount() - 1;
                Group group = null;
                try {
                    group = department.getGroup((Integer) studentsTable.getValueAt(row, codeColumn));
                    oldName = group.name;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                int groupIndex = department.groups.indexOf(group);


                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = studentsTable.getColumnName(column);
                    if (columnName.equals("name")) {
                        department.students.get(row).name = (String) studentsTable.getValueAt(row, column);
                        System.out.println(department.students.get(row).name);

                    }

                    if (columnName.equals("is_captain")) {
                        department.students.get(row).isCaptain = Boolean.parseBoolean((String) studentsTable.getValueAt(row, column));
                    }

                    if (columnName.equals("group")) {
                        String newName = (String) studentsTable.getValueAt(row, column);

                        department.groups.get(groupIndex).name = newName;

                        for (int i = 0; i < studentsTable.getRowCount(); i++) {
                            if ((studentsTable.getValueAt(i, column)).equals(oldName)) {
                                studentsTable.setValueAt(newName, i, column);
                                studentsTable.updateUI();
                            }
                        }
                    }
                }
                studentsTable.getModel().addTableModelListener(this);
            }
        });
    }

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

    }

    //----------------------------------------------

    private void initResultPane() {
        resultTextPane = new JTextPane();
        resultTextPane.setText("Here you will see results. " +
                "\n\n Select row to delete it. " +
                "\n Double click on the cell to edit it.");
        resultTextPane.setBounds(5, 450, 490, 100);

        resultTextPane.setEditable(false);
        add(resultTextPane);
    }

    private void initButtons() {

        addStudentButton = new JButton("Add Student");
        addStudentButton.setBounds(400, 300, 90, 20);
        addStudentButton.addActionListener((e) -> resultTextPane.setText("New student was added!"));

        add(addStudentButton);

        addGroupButton = new JButton("Add Group");
        addGroupButton.setBounds(400, 330, 90, 20);
        addGroupButton.addActionListener((e) -> resultTextPane.setText("New group was added!"));

        add(addGroupButton);

        // -------------------------------------------------------------

        findStudentButton = new JButton("Find Student");
        findStudentButton.setBounds(110, 370, 90, 20);
        findStudentButton.addActionListener((e) -> resultTextPane.setText("Student: "));

        add(findStudentButton);

        findGroupButton = new JButton("Find Group");
        findGroupButton.setBounds(400, 370, 90, 20);
        findGroupButton.addActionListener((e) -> resultTextPane.setText("Group: "));

        add(findGroupButton);

        findStudentsInGroupButton = new JButton("Students in Group");
        findStudentsInGroupButton.setBounds(400, 400, 90, 20);
        findStudentsInGroupButton.addActionListener((e) -> resultTextPane.setText("Students in group: "));

        add(findStudentsInGroupButton);

        // -------------------------------------------------------------

        deleteRowButton = new JButton("Delete Row");
        deleteRowButton.setBounds(5, 560, 90, 20);
        deleteRowButton.addActionListener((e) -> resultTextPane.setText("Row was deleted!"));
        deleteRowButton.setEnabled(false);

        add(deleteRowButton);

        saveToFileButton = new JButton("Save");
        saveToFileButton.setBounds(400, 560, 90, 20);
        saveToFileButton.addActionListener((e) -> department.saveToFile(department.OUTPUT_FILE));

        add(saveToFileButton);
    }

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

    //----------------------------------------------

    public static void main(String[] args) {
        new MainForm();
    }
}
