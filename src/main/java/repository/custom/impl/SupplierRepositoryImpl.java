package repository.custom.impl;

import model.Supplier;
import repository.custom.SupplierRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {

    @Override
    public String getLastId() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM supplier ORDER BY id DESC LIMIT 1");
        if(resultSet.next()){
            return  resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean create(Supplier supplier) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException {
        return false;
    }

    @Override
    public Supplier getById(String s) throws SQLException {
        return null;
    }

    @Override
    public List<Supplier> getAll() throws SQLException {
        return List.of();
    }
}
