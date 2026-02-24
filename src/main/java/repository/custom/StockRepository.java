package repository.custom;

import model.Stock;
import repository.SuperRepository;

import java.sql.Connection;

public interface StockRepository extends SuperRepository {

    int saveAndGetId(Stock stock, Connection connection) throws Exception;

    int isExists(Stock stock, Connection connection) throws Exception;

    boolean updateQty(int stockId, double qty, Connection connection) throws Exception;
}

