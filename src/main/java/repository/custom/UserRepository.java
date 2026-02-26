package repository.custom;

import model.Supplier;
import model.User;
import repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,String> {

    String getId() throws Exception;

    List<String> getAllStatus() throws Exception;

    List<String> getUserRole() throws Exception;

    boolean isDuplicateUser(int id, String username, String password, String role) throws Exception;

    User login(String username, String password) throws Exception;

}
