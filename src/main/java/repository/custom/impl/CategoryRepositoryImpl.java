package repository.custom.impl;

import model.Category;
import repository.custom.CategoryRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public boolean create(Category category, int statusId) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Category category, int statusId) throws Exception {
        return false;
    }

    @Override
    public Category getById(Integer integer) throws SQLException {
        return null;
    }

    @Override
    public List<Category> getAll() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category");
        List<Category> categoryList = new ArrayList<>();

        while (resultSet.next()) {
            categoryList.add(new Category(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
            ));
        }
        return categoryList;
    }
}
