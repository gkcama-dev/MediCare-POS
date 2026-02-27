package service.custom.impl;

import database.DbConnection;
import model.Product;
import net.sf.jasperreports.engine.*;
import repository.RepositoryFactory;
import repository.custom.ProductRepository;
import service.custom.ProductService;
import util.RepositoryType;

import java.sql.Connection;
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

            String numericPart = lastId.replaceAll("[^0-9]", "");

            if (!numericPart.isEmpty()) {
                int id = Integer.parseInt(numericPart);
                id++;

                return String.format("P%03d", id);
            }
        }

        return "P001";
    }

    @Override
    public List<String> getProductStatus() throws Exception {
        return productRepository.getAllStatus();
    }

    @Override
    public JasperPrint generateAllProductReport() throws Exception {

        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/MedicarePOS-All-Products-Report.jrxml")
        );

        Connection connection = DbConnection.getInstance().getConnection();

        return JasperFillManager.fillReport(
                jasperReport,
                null,
                connection
        );
    }
}
