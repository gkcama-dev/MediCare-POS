package service.custom.impl;

import model.Brand;
import model.Supplier;
import repository.RepositoryFactory;
import repository.custom.BrandRepository;
import service.ServiceFactory;
import service.custom.BrandService;
import util.RepositoryType;
import util.ServiceType;

import java.sql.SQLException;
import java.util.List;

public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.BRAND);

    @Override
    public boolean addBrand(Brand brand) throws Exception {
        return false;
    }

    @Override
    public boolean updateBrand(Brand brand) throws Exception {
        return false;
    }

    @Override
    public Brand searchBrand(String id) throws SQLException {
        return null;
    }

    @Override
    public List<Brand> getAllBrand() throws Exception {
        return brandRepository.getAll();
    }
}
