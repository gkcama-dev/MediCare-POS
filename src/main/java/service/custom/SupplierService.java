package service.custom;

import model.Supplier;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface SupplierService extends SuperService {
    boolean addSupplier(Supplier supplier) throws SQLException;

    boolean updateSupplier(Supplier supplier) throws SQLException;

    Supplier searchSupplier(String id) throws SQLException;

    List<Supplier> getAllSupplier() throws SQLException;

    List<String> getAllSupplierId() throws SQLException;

    String generateNextSupplierId() throws Exception;
}
