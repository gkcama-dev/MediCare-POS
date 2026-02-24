package service.custom.impl;

import model.User;
import service.custom.UserService;

import java.util.List;
import java.util.function.UnaryOperator;

public class UserServiceImpl implements UserService {
    @Override
    public boolean addUser(User user) throws Exception {
        return false;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        return false;
    }

    @Override
    public List<User> getAllUser() throws Exception {
        return List.of();
    }

    @Override
    public List<String> getUserStatus() throws Exception {
        return List.of();
    }

    @Override
    public List<String> getUserRole() throws Exception {
        return List.of();
    }
}
