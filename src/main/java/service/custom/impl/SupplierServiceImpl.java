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
        int statusId = supplier.getStatus().equalsIgnoreCase("Active") ? 1 : 2;
        return supplierRepository.create(supplier,statusId);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) throws Exception {

        if(supplierRepository.isDuplicateEmailOrMobile(supplier.getId(), supplier.getEmail(),supplier.getMobile())){
            throw new SQLException("Email or Mobile already exists for another supplier!");
        }

        int statusId = supplier.getStatus().equalsIgnoreCase("Active") ? 1 : 2;
        return supplierRepository.update(supplier,statusId);
    }

    @Override
    public Supplier searchSupplier(String id) throws SQLException {
        return null;
    }

    @Override
    public List<Supplier> getAllSupplier() throws Exception {
        return supplierRepository.getAll();
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
            id++;
            return String.format("S%03d", id);
        }
        return "S001";
    }

    @Override
    public List<String> getSupplierStatus() throws Exception {
        return supplierRepository.getAllStatus();
    }
}
