package service.custom.impl;

import model.Category;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import service.custom.CategoryService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.CATEGORY);

    @Override
    public boolean addCategory(Category category) throws Exception {
        return false;
    }

    @Override
    public boolean updateCategory(Category category) throws Exception {
        return false;
    }

    @Override
    public Category searchCategory(String id) throws SQLException {
        return null;
    }

    @Override
    public List<Category> getAllCategory() throws Exception {
        return categoryRepository.getAll();
    }
}
