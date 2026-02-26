package repository.custom;

import model.Stock;
import model.tableModel.StockTM;
import repository.SuperRepository;

import java.sql.Connection;
import java.util.List;

public interface StockRepository extends SuperRepository {

    int saveAndGetId(Stock stock, Connection connection) throws Exception;

    int isExists(Stock stock, Connection connection) throws Exception;

    boolean updateQty(int stockId, double qty, Connection connection) throws Exception;

    List<Stock> getAllStock() throws Exception;

    boolean reduceQty(int stockId, double qty, Connection connection) throws Exception;

    List<StockTM> getAllStockForStockView() throws Exception;
}

