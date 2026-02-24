package repository.custom;

import model.GRN;
import model.GRNItem;
import repository.SuperRepository;

import java.sql.Connection;

public interface GRNItemRepository extends SuperRepository {
    boolean save(GRNItem item, Connection connection) throws Exception;
}
