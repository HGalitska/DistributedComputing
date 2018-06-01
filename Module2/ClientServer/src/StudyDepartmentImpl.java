import dao.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class StudyDepartmentImpl extends UnicastRemoteObject implements StudyDepartment {

    private ArrayList<Group> groups;
    private ArrayList<Student> students;

    private final StudentDao studentDao = new StudentDaoImpl();
    private final GroupDao groupDao = new GroupDaoImpl();

    StudyDepartmentImpl() throws RemoteException {
        groups = new ArrayList<>(groupDao.getAllGroups());
        students = new ArrayList<>(studentDao.getAllStudents());
    }

    @Override
    public List<Group> getAllGroups() {
        synchronized (groups) {
            return groups;
        }
    }

    @Override
    public Group getGroup(int groupId) {

        synchronized (groups) {
            System.out.println(groups);
            for (Group group : groups) {
                if (group.code == groupId) {
                    return group;
                }
            }
            return null;
        }

    }

    @Override
    public void addGroup(Group group) {
        synchronized (groups) {
            groups.add(group);
        }
    }

    @Override
    public void updateGroup(Group group) {
        synchronized (groups) {
            for (Group g : groups) {
                if (g.code == group.code) {
                    g.name = group.name;
                }
            }
        }
    }

    @Override
    public void deleteGroup(int groupId) {
        synchronized (groups) {
            Group toDelete = null;
            for (Group group : groups) {
                if (group.code == groupId) {
                    toDelete = group;
                    break;
                }
            }
            groups.remove(toDelete);
            groupDao.deleteGroup(groupId);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        synchronized (students) {
            return students;
        }
    }

    @Override
    public Student getStudent(int studentId) {
        Student result = null;
        synchronized (students) {
            for (Student student : students) {
                if (student.code == studentId) {
                    result = student;
                }
            }
        }
        return result;
    }

    @Override
    public void addStudent(Student student) {
        synchronized (students) {
            students.add(student);
        }
    }

    @Override
    public void updateStudent(Student student) {
        synchronized (students) {
            for (Student s : students) {
                if (s.code == student.code) {
                    s.name = student.name;
                    s.groupId = student.groupId;
                }
            }
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        synchronized (students) {
            Student toDelete = null;
            for (Student student : students) {
                if (student.code == studentId) {
                    toDelete = student;
                    break;
                }
            }
            students.remove(toDelete);
            studentDao.deleteStudent(studentId);
        }
    }

    @Override
    public void saveToDB() {
        synchronized (groups) {
            groupDao.saveGroupsToDB(groups);
        }
        synchronized (students) {
            studentDao.saveStudentsToDB(students);
        }
    }

    public void update() throws RemoteException{
        groups = new ArrayList<>(groupDao.getAllGroups());
        students = new ArrayList<>(studentDao.getAllStudents());
    }
}
