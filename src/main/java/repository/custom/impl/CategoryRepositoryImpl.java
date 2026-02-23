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
    public boolean create(Category category) throws Exception {
        return CrudUtil.execute("INSERT INTO category (id, name) VALUES (?, ?)", category.getId(), category.getName());
    }

    @Override
    public boolean update(Category category, int statusId) throws Exception {
        return false;
    }

    @Override
    public boolean update(Category category) throws Exception {
        return CrudUtil.execute("UPDATE category SET name = ? WHERE id = ?", category.getName(), category.getId());
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
            categoryList.add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
        }
        return categoryList;
    }

    @Override
    public boolean isDuplicateCategory(String name) throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM category WHERE name = ?", name);
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public String getId() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM category ORDER BY id DESC LIMIT 1");
        if (resultSet.next()) {
            int nextId = Integer.parseInt(resultSet.getString(1)) + 1;
            return String.valueOf(nextId);
        }
        return "1";
    }
}
