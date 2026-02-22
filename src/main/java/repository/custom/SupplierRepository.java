package repository.custom;

import model.Supplier;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface SupplierRepository extends CrudRepository<Supplier,String> {

    String getLastId() throws Exception;

    List<String> getAllStatus() throws Exception;

    boolean isDuplicateEmailOrMobile(String id,String Email,String Mobile) throws Exception;
}
