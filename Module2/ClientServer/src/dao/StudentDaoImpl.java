package dao;

import connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    @Override
    public List<Student> getAllStudents() {
        ArrayList<Student> result = new ArrayList<>();

        String sql = "SELECT * FROM studydep.students";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("name");
                int groupId = rs.getInt("groupCode");
                result.add(new Student(code, name, groupId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }

        return result;
    }

    @Override
    public Student getStudent(int studentId) {
        Student result = null;

        String sql = "SELECT * FROM studydep.students WHERE students.code = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("name");
                int groupId = rs.getInt("groupCode");
                result = new Student(code, name, groupId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }

        return result;
    }

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO studydep.students(code, name, groupId) VALUES(?, ?, ?)";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, student.code);
            statement.setString(2, student.name);
            statement.setInt(1, student.groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE studydep.students SET name = ?, groupId = ? WHERE code = ?";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, student.name);
            statement.setInt(2, student.groupId);
            statement.setInt(3, student.code);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        String sql = "DELETE FROM studydep.students WHERE code = ?";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }
}
