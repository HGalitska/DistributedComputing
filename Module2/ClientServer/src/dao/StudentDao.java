package dao;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {
    List<Student> getAllStudents();
    Student getStudent(int studentId);
    void addStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(int studentId);
    void saveStudentsToDB(ArrayList<Student> students);
}
