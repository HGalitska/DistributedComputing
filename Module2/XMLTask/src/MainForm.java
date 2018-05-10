import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {

    StudiesDepartment department;

    JTable dataTable;
    JScrollPane sp;
    JTabbedPane pane;

    JButton saveToFileButton;
    JButton addRowButton;
    JButton deleteRowButton;

    MainForm() {
        setLayout(null);

        department = new StudiesDepartment();

        initTable();
        init();

        setSize(500, 500);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        dataTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                dataTable.getModel().removeTableModelListener(this);
                int row = e.getFirstRow();
                int column = e.getColumn();
                String oldName = "2";

                int codeColumn = dataTable.getColumnCount() - 1;
                Group group = null;
                try {
                    group = department.getGroup((Integer) dataTable.getValueAt(row, codeColumn));
                    oldName = group.name;
                    } catch (Exception e1) {
                    e1.printStackTrace();
                }
                int groupIndex = department.groups.indexOf(group);


                if (e.getType() == TableModelEvent.UPDATE) {
                    String columnName = dataTable.getColumnName(column);
                    if (columnName.equals("name")) {
                        department.students.get(row).name = (String) dataTable.getValueAt(row, column);
                        System.out.println(department.students.get(row).name);

                    }

                    if (columnName.equals("is_captain")) {
                        department.students.get(row).isCaptain = Boolean.parseBoolean((String) dataTable.getValueAt(row, column));
                    }

                    if (columnName.equals("group")) {
                        String newName = (String) dataTable.getValueAt(row, column);

                        department.groups.get(groupIndex).name = newName;

                        for (int i = 0; i < dataTable.getRowCount(); i++) {
                            if ((dataTable.getValueAt(i, column)).equals(oldName)) {
                                dataTable.setValueAt((Object) newName, i, column);
                                dataTable.updateUI();
                            }
                        }

                    }
                }

                if (e.getType() == TableModelEvent.INSERT) {

                }
                dataTable.getModel().addTableModelListener(this);
            }
        });
    }

    private void initTable() {
        String[] studentsColumns = {"code", "name", "is_captain", "group", "group_code"};
        Object[] rowData = new Object[5];
        DefaultTableModel studentsModel = new DefaultTableModel();
        studentsModel.setColumnIdentifiers(studentsColumns);
        for (int i = 0; i < department.students.size(); i++) {
            rowData[0] = department.students.get(i).code;
            rowData[1] = department.students.get(i).name;
            rowData[2] = department.students.get(i).isCaptain;
            rowData[3] = department.students.get(i).group.name;
            rowData[4] = department.students.get(i).group.code;
            studentsModel.addRow(rowData);
        }
        dataTable = new JTable(studentsModel);
    }

    private void init() {
        sp = new JScrollPane(dataTable);

        dataTable.setFillsViewportHeight(true);
        dataTable.setColumnSelectionAllowed(false);
        dataTable.setRowSelectionAllowed(true);
        pane = new JTabbedPane();
        pane.setBounds(0, 0, 500, 300);
        pane.add("Study Department #2938", sp);
        dataTable.setBackground(new Color(246, 217, 221));
        add(pane);

        saveToFileButton = new JButton("Save");
        saveToFileButton.setBounds(400, 400, 90, 20);
        saveToFileButton.addActionListener((e) -> department.saveToFile(department.OUTPUT_FILE));

        add(saveToFileButton);

        addRowButton = new JButton("Add Row");
        addRowButton.setBounds(5, 400, 90, 20);
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        addRowButton.addActionListener((e) -> {
            model.addRow(new Object[]{department.students.size() + 1, "", "", "", department.groups.size()});
            department.students.add(new Student());
            department.students.get(department.students.size() - 1).code = department.students.size();
            department.students.get(department.students.size() - 1).group = department.groups.get(department.groups.size() - 1);
            System.out.println(department.students.size());
        });

        add(addRowButton);

        deleteRowButton = new JButton("Add Row");
        deleteRowButton.setBounds(95, 400, 90, 20);
        deleteRowButton.addActionListener((e) -> {
            model.removeRow(dataTable.getSelectedRow());
            try {
                department.students.remove(department.getStudent((Integer) dataTable.getValueAt(dataTable.getSelectedRows()[0], 0)));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        add(deleteRowButton);

    }

    public static void main(String[] args) {
        new MainForm();
    }
}
