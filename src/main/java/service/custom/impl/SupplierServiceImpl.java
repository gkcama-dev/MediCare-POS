package service.custom.impl;

import model.Supplier;
import repository.RepositoryFactory;
import repository.custom.SupplierRepository;
import service.custom.SupplierService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.SUPPLIER);

    @Override
    public boolean addSupplier(Supplier supplier) throws SQLException {
        return false;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) throws SQLException {
        return false;
    }

    @Override
    public Supplier searchSupplier(String id) throws SQLException {
        return null;
    }

    @Override
    public List<Supplier> getAllSupplier() throws SQLException {
        return List.of();
    }

    @Override
    public List<String> getAllSupplierId() throws SQLException {
        return List.of();
    }

    @Override
    public String generateNextSupplierId() throws Exception {
        String lastId = supplierRepository.getLastId();
        if (lastId != null) {
            int id = Integer.parseInt(lastId.replace("S", ""));
            return String.format("S%03d", id);
        }
        return "S001";
    }
}
