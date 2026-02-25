package repository.custom;

import model.Product;
import repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,String> {
    String getLastProductCode() throws Exception;

    List<String> getAllStatus() throws Exception;

    boolean isDuplicateProduct(String id,String name,String brand,String category) throws Exception;

    boolean isMedicineActive(String productCode) throws Exception;
}
