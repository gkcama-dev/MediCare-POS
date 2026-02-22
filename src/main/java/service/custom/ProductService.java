package service.custom;

import model.Product;
import model.Supplier;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface ProductService extends SuperService {
    boolean addProduct(Product product) throws Exception;

    boolean updateProduct(Product product) throws Exception;

    List<Product> getAllProduct() throws Exception;

    String generateNextProductId() throws Exception;

    List<String> getProductStatus() throws Exception;

}
