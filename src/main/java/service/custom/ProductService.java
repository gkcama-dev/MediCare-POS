package service.custom;

import model.Product;
import net.sf.jasperreports.engine.JasperPrint;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {
    boolean addProduct(Product product) throws Exception;

    boolean updateProduct(Product product) throws Exception;

    List<Product> getAllProduct() throws Exception;

    String generateNextProductId() throws Exception;

    List<String> getProductStatus() throws Exception;

    JasperPrint generateAllProductReport() throws Exception;

}
