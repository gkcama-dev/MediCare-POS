package repository.custom.impl;

import model.User;
import repository.custom.UserRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public boolean create(User user, int statusId) throws SQLException {
        try {
            return CrudUtil.execute(
                    "INSERT INTO user (id, username, password, user_type_id, status_id) VALUES (?,?,?,?,?)",
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    getRoleId(user.getUserType()),
                    statusId
            );
        } catch (Exception e) {
            throw new SQLException("Add Failed: " + e.getMessage());
        }
    }

    private int getRoleId(String roleName) throws Exception {
        ResultSet rs = CrudUtil.execute("SELECT id FROM user_type WHERE user_type = ?", roleName);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Invalid Role!");
    }

    @Override
    public boolean create(User user) throws Exception {
        return false;
    }

    @Override
    public boolean update(User user, int statusId) throws Exception {
        try {
            return CrudUtil.execute(
                    "UPDATE user SET username=?, password=?, user_type_id=?, status_id=? WHERE id=?",
                    user.getUsername(),
                    user.getPassword(),
                    getRoleId(user.getUserType()),
                    statusId,
                    user.getId()
            );
        } catch (Exception e) {
            throw new SQLException("Update Failed: " + e.getMessage());
        }
    }

    @Override
    public boolean update(User user) throws Exception {
        return false;
    }

    @Override
    public User getById(String s) throws SQLException {
        return null;
    }

    @Override
    public List<User> getAll() throws Exception {
        ResultSet rs = CrudUtil.execute(
                "SELECT u.id, u.username, u.password, ut.user_type, st.status " +
                        "FROM user u " +
                        "JOIN user_type ut ON u.user_type_id = ut.id " +
                        "JOIN status st ON u.status_id = st.id"
        );

        List<User> userList = new ArrayList<>();

        while (rs.next()) {
            userList.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("user_type"),
                    rs.getString("status")
            ));
        }

        return userList;
    }

    @Override
    public String getId() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM user ORDER BY id DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            int nextId = Integer.parseInt(lastId) + 1;

            return String.valueOf(nextId);
        }

        return "1";
    }

    @Override
    public List<String> getAllStatus() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT status from status");
        List<String> statuses = new ArrayList<>();
        while (resultSet.next()) {
            statuses.add(resultSet.getString(1));
        }
        return statuses;
    }

    @Override
    public List<String> getUserRole() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT user_type FROM user_type");
        List<String> roles = new ArrayList<>();
        while (resultSet.next()) {
            roles.add(resultSet.getString(1));
        }
        return roles;
    }

    @Override
    public boolean isDuplicateUser(int id, String username, String password, String role) throws Exception {

        String sql = "SELECT u.id FROM user u " +
                "JOIN user_type ut ON u.user_type_id = ut.id " +
                "WHERE u.username = ? AND u.password = ? AND ut.user_type = ? " +
                "AND u.id != ?";

        ResultSet resultSet = CrudUtil.execute(sql, username, password, role, id);

        return resultSet.next();
    }
}
