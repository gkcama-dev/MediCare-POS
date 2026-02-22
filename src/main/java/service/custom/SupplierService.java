package service.custom;

import model.Supplier;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface SupplierService extends SuperService {
    boolean addSupplier(Supplier supplier) throws Exception;

    boolean updateSupplier(Supplier supplier) throws Exception;

    Supplier searchSupplier(String id) throws SQLException;

    List<Supplier> getAllSupplier() throws Exception;

    List<String> getAllSupplierId() throws SQLException;

    String generateNextSupplierId() throws Exception;

    List<String> getSupplierStatus() throws Exception;
}
