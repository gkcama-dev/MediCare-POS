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
    public boolean update(Brand brand, int statusId) throws Exception {
        return false;
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
}
