package service.custom;

import model.Supplier;
import model.User;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface UserService extends SuperService {

    boolean addUser(User user) throws Exception;

    boolean updateUser(User user) throws Exception;

    List<User> getAllUser() throws Exception;

    String getUserId() throws Exception;


    List<String> getUserStatus() throws Exception;

    List<String> getUserRole() throws Exception;

    User login(String username, String password) throws Exception;
}
