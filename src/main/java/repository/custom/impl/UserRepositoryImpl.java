package repository.custom.impl;

import model.User;
import repository.custom.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public boolean create(User user, int statusId) throws SQLException {
        return false;
    }

    @Override
    public boolean create(User user) throws Exception {
        return false;
    }

    @Override
    public boolean update(User user, int statusId) throws Exception {
        return false;
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
        return List.of();
    }
}
