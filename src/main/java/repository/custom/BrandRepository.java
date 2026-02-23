package repository.custom;

import model.Brand;
import repository.CrudRepository;

public interface BrandRepository extends CrudRepository<Brand, Integer> {

    boolean isDuplicateBrand(String name) throws Exception;

    String getId() throws Exception;
}
