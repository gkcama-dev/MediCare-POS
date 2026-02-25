package repository.custom.impl;

import model.Supplier;
import repository.custom.SupplierRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {

    @Override
    public String getLastId() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM supplier ORDER BY id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public List<String> getAllStatus() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT status from status");
        List<String> statuses = new ArrayList<>();
        while (resultSet.next()) {
            statuses.add(resultSet.getString(1));
        }
        return statuses;
    }

    @Override
    public boolean isDuplicateEmailOrMobile(String id, String email, String mobile) throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM supplier WHERE (email = ? OR mobile = ?) AND (id != ? OR ? IS NULL)",
                email, mobile, id, id);
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public boolean isSupplierActive(String supplierId) throws Exception {
        String sql = "SELECT st.status FROM supplier s " +
                "JOIN status st ON s.status_id = st.id " +
                "WHERE s.id = ?";

        ResultSet rs = CrudUtil.execute(sql, supplierId);

        if (rs.next()) {
            return rs.getString("status").equalsIgnoreCase("Active");
        }
        return false;
    }

    @Override
    public boolean create(Supplier supplier, int statusId) throws SQLException {
        try {
            return CrudUtil.execute("INSERT INTO supplier VALUES (?,?,?,?,?,?,?)",
                    supplier.getId(),
                    supplier.getFirstName(),
                    supplier.getLastName(),
                    supplier.getCompany(),
                    supplier.getMobile(),
                    supplier.getEmail(),
                    statusId
            );
        } catch (Exception e) {
            throw new SQLException("Add Failed: " + e.getMessage());
        }
    }

    @Override
    public boolean create(Supplier supplier) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Supplier supplier, int statusId) throws Exception {
        try {
            return CrudUtil.execute("UPDATE supplier SET first_name=?, last_name=?, company=?, mobile=?, email=?, status_id=? WHERE id=?",
                    supplier.getFirstName(),
                    supplier.getLastName(),
                    supplier.getCompany(),
                    supplier.getMobile(),
                    supplier.getEmail(),
                    statusId,
                    supplier.getId()
            );
        } catch (Exception e) {
            throw new SQLException("Update Failed: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Supplier supplier) throws Exception {
        return false;
    }

    @Override
    public Supplier getById(String s) throws SQLException {
        return null;
    }

    @Override
    public List<Supplier> getAll() throws Exception {

        ResultSet resultSet = CrudUtil.execute("SELECT s.id, s.first_name, s.last_name, s.company, s.mobile, s.email, st.status" +
                " FROM supplier s INNER JOIN status st ON" +
                " s.status_id = st.id;");

        ArrayList<Supplier> supplierList = new ArrayList<>();

        while (resultSet.next()) {
            supplierList.add(new Supplier(
                    resultSet.getString("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("company"),
                    resultSet.getString("mobile"),
                    resultSet.getString("email"),
                    resultSet.getString("status")
            ));
        }
        return supplierList;
    }
}
