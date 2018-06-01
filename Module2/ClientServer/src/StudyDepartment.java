import dao.Group;
import dao.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudyDepartment extends Remote {
    List<Group> getAllGroups() throws RemoteException;
    Group       getGroup(int groupId) throws RemoteException;
    void        addGroup(Group group) throws RemoteException;
    void        updateGroup(Group group) throws RemoteException;
    void        deleteGroup(int groupId) throws RemoteException;

    List<Student> getAllStudents() throws RemoteException;
    Student       getStudent(int studentId) throws RemoteException;
    void          addStudent(Student student) throws RemoteException;
    void          updateStudent(Student student) throws RemoteException;
    void          deleteStudent(int studentId) throws RemoteException;

    void saveToDB() throws RemoteException;
    void update() throws RemoteException;
}
