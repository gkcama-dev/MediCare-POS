package repository.custom;

import model.Supplier;
import repository.CrudRepository;

import java.sql.SQLException;

public interface SupplierRepository extends CrudRepository<Supplier,String> {

    String getLastId() throws Exception;


}
