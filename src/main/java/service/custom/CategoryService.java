package service.custom;

import model.Brand;
import model.Category;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService extends SuperService {
    boolean addCategory(Category category) throws Exception;

    boolean updateCategory(Category category) throws Exception;

    Category searchCategory(String id) throws SQLException;

    List<Category> getAllCategory() throws Exception;

    String getCategoryId() throws Exception;
}
