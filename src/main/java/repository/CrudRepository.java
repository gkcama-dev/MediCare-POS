package repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T,ID> extends SuperRepository {
    boolean create(T t, int statusId)throws SQLException;
    boolean update(T t,int statusId) throws Exception;
    T getById(ID id)throws SQLException;
    List<T> getAll() throws Exception;
}
