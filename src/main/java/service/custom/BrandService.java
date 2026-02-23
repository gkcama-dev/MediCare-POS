package service.custom;

import model.Brand;
import model.Supplier;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface BrandService extends SuperService {
    boolean addBrand(Brand brand) throws Exception;

    boolean updateBrand(Brand brand) throws Exception;

    Brand searchBrand(String id) throws SQLException;

    List<Brand> getAllBrand() throws Exception;

    String getBrandId() throws Exception;
}
