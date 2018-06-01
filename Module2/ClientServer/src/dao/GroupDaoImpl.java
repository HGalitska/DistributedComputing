package dao;

import connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {
    @Override
    public List<Group> getAllGroups() {
        ArrayList<Group> result = new ArrayList<>();

        String sql = "SELECT * FROM studydep.groups";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("name");
                result.add(new Group(code, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }

        return result;
    }

    @Override
    public Group getGroup(int groupId) {
        Group result = null;

        String sql = "SELECT * FROM studydep.groups WHERE groups.code = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, groupId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("name");
                result = new Group(code, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }

        return result;
    }

    @Override
    public void addGroup(Group group) {
        String sql = "INSERT INTO studydep.groups(code, name) VALUES(?, ?)";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, group.code);
            statement.setString(2, group.name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }

    @Override
    public void updateGroup(Group group) {
        String sql = "UPDATE studydep.groups SET name = ? WHERE code = ?";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, group.name);
            statement.setInt(2, group.code);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }

    @Override
    public void deleteGroup(int groupId) {
        String sql = "DELETE FROM studydep.groups WHERE code = ?";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(statement, connection);
        }
    }

    public void saveGroupsToDB(ArrayList<Group> groups) {

        int countRows = getAllGroups().size();

        while (countRows < groups.size()) {
            addGroup(groups.get(countRows));
            countRows++;
        }

        for (Group g : groups) {
            updateGroup(g);
        }
    }
}
