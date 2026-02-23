package repository.custom.impl;

import model.Brand;
import repository.custom.BrandRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandRepositoryImpl implements BrandRepository {
    @Override
    public boolean create(Brand brand, int statusId) throws SQLException {
        return false;
    }

    @Override
    public boolean create(Brand brand) throws Exception {
        return CrudUtil.execute("INSERT INTO brand (name) VALUES (?)",
                brand.getName()
        );
    }

    @Override
    public boolean update(Brand brand, int statusId) throws Exception {
        return false;
    }

    @Override
    public boolean update(Brand brand) throws Exception {
        return CrudUtil.execute("UPDATE brand SET name = ? WHERE id = ?",
                brand.getName(),
                brand.getId()
                );
    }

    @Override
    public Brand getById(Integer integer) throws SQLException {
        return null;
    }

    @Override
    public List<Brand> getAll() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM brand");

        List<Brand> brandList = new ArrayList<>();
        while (resultSet.next()) {
            brandList.add(new Brand(
                    resultSet.getInt("id"),
                    resultSet.getString("name"))
            );
        }
        return brandList;
    }

    @Override
    public boolean isDuplicateBrand(String name) throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM brand WHERE name = ?", name);
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public String getId() throws Exception {

        ResultSet resultSet = CrudUtil.execute("SELECT id FROM brand ORDER BY id DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            int nextId = Integer.parseInt(lastId) + 1;

            return String.valueOf(nextId);
        }

        return "1";
    }
}
