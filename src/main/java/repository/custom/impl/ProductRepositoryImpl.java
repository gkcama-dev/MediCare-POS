package repository.custom.impl;

import model.Product;
import repository.custom.ProductRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public boolean create(Product product, int statusId) throws SQLException {
        try {
            return CrudUtil.execute("INSERT INTO product VALUES (?,?, (SELECT id FROM brand WHERE name = ?), (SELECT id FROM category WHERE name = ?),?)",
                    product.getCode(),
                    product.getName(),
                    product.getBrand(),
                    product.getCategory(),
                    statusId
            );
        } catch (Exception e) {
            throw new SQLException("Add Failed: " + e.getMessage());
        }
    }

    @Override
    public boolean create(Product product) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Product product, int statusId) throws Exception {
        return CrudUtil.execute("UPDATE product SET name = ?," +
                        " brand_id = (SELECT id FROM brand WHERE name = ?), " +
                        "category_id = (SELECT id FROM category WHERE name = ?), " +
                        "status_id = ? WHERE code = ?",
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                statusId,
                product.getCode()
        );
    }

    @Override
    public boolean update(Product product) throws Exception {
        return false;
    }

    @Override
    public Product getById(String s) throws SQLException {
        return null;
    }

    @Override
    public List<Product> getAll() throws Exception {

        ResultSet resultSet = CrudUtil.execute("SELECT p.code, p.name, b.name AS brand, c.name AS category, s.status FROM " +
                "product p JOIN brand b ON p.brand_id = b.id JOIN " +
                "category c ON p.category_id = c.id JOIN status s ON p.status_id = s.id");

        List<Product> productList = new ArrayList<>();

        while (resultSet.next()) {
            productList.add(new Product(
                    resultSet.getString("code"),
                    resultSet.getString("name"),
                    resultSet.getString("brand"),
                    resultSet.getString("category"),
                    resultSet.getString("status")
            ));
        }

        return productList;
    }

    @Override
    public String getLastProductCode() throws Exception {
        String sql = "SELECT code FROM product ORDER BY LENGTH(code) DESC, code DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);

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
    public boolean isDuplicateProduct(String id, String name, String brand, String category) throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM product p " +
                        "JOIN brand b ON p.brand_id = b.id " +
                        "JOIN category c ON p.category_id = c.id " +
                        "WHERE p.name = ? AND b.name = ? AND c.name = ? AND p.code != ?",
                name, brand, category, id);

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }

        return false;
    }

    @Override
    public boolean isMedicineActive(String productCode) throws Exception {
        String sql =
                "SELECT s.status FROM product p " +
                        "JOIN status s ON p.status_id = s.id " +
                        "WHERE p.code = ?";

        ResultSet rs = CrudUtil.execute(sql, productCode);

        if (rs.next()) {
            return rs.getString("status").equalsIgnoreCase("Active");
        }
        return false;
    }
}
