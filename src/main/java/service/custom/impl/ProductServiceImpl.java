package service.custom.impl;

import model.Product;
import repository.RepositoryFactory;
import repository.custom.ProductRepository;
import service.custom.ProductService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.PRODUCT);

    @Override
    public boolean addProduct(Product product) throws Exception {

        if(productRepository.isDuplicateProduct(null,product.getName(),product.getBrand(), product.getCategory())){
            throw new SQLException("This Product already exists in the system!");
        }

        int statusId = product.getStatus().equalsIgnoreCase("Active") ? 1 : 2;
        return productRepository.create(product,statusId);
    }

    @Override
    public boolean updateProduct(Product product) throws Exception {
        if(productRepository.isDuplicateProduct(product.getCode(),product.getName(),product.getBrand(), product.getCategory())){
            throw new SQLException("This Product (Name, Brand, Category combination) already exists!");
        }

        int statusId = product.getStatus().equalsIgnoreCase("Active") ? 1 : 2;
        return productRepository.update(product,statusId);
    }

    @Override
    public List<Product> getAllProduct() throws Exception {
        return productRepository.getAll();
    }

    @Override
    public String generateNextProductId() throws Exception {

        String lastId = productRepository.getLastProductCode();

        if (lastId != null) {
            int id = Integer.parseInt(lastId.replace("#MP", ""));
            id++;

            return String.format("#MP%03d", id);
        }

        return "#MP001";
    }

    @Override
    public List<String> getProductStatus() throws Exception {
        return productRepository.getAllStatus();
    }
}
