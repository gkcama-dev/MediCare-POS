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
        if (categoryRepository.isDuplicateCategory(category.getName())) {
            throw new Exception("Category '" + category.getName() + "' already exists!");
        }
        return categoryRepository.create(category);
    }

    @Override
    public boolean updateCategory(Category category) throws Exception {
        if (categoryRepository.isDuplicateCategory(category.getName())) {
            throw new Exception("Category '" + category.getName() + "' already exists!");
        }
        return categoryRepository.update(category);
    }

    @Override
    public Category searchCategory(String id) throws SQLException {
        return null;
    }

    @Override
    public List<Category> getAllCategory() throws Exception {
        return categoryRepository.getAll();
    }

    @Override
    public String getCategoryId() throws Exception {
        return categoryRepository.getId();
    }
}
