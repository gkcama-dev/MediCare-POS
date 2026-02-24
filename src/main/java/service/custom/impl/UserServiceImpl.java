package service.custom.impl;

import model.User;
import repository.RepositoryFactory;
import repository.custom.UserRepository;
import service.custom.UserService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;
import java.util.function.UnaryOperator;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.USER);

    @Override
    public boolean addUser(User user) throws Exception {

        if (userRepository.isDuplicateUser(0, user.getUsername(), user.getPassword() , user.getUserType())) {
            throw new SQLException("This exact user profile already exists!");
        }

        int statusId = user.getStatus().equalsIgnoreCase("Active") ? 1 : 2;
        return userRepository.create(user,statusId);
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        if (userRepository.isDuplicateUser(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getUserType())) {

            throw new SQLException("Username already exists!");
        }

        int statusId = user.getStatus().equalsIgnoreCase("Active") ? 1 : 2;

        return userRepository.update(user, statusId);
    }

    @Override
    public List<User> getAllUser() throws Exception {
        return userRepository.getAll();
    }

    @Override
    public String getUserId() throws Exception {
        return userRepository.getId();
    }

    @Override
    public List<String> getUserStatus() throws Exception {
        return userRepository.getAllStatus();
    }

    @Override
    public List<String> getUserRole() throws Exception {
        return userRepository.getUserRole();
    }
}
