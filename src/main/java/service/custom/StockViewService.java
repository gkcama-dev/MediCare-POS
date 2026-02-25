package service.custom;

import model.Stock;
import service.SuperService;

import java.util.List;

public interface StockViewService extends SuperService {

    List<Stock> getAllStock() throws Exception;

}
