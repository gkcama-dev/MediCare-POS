package service.custom;

import model.Stock;
import model.tableModel.StockTM;
import net.sf.jasperreports.engine.JasperPrint;
import service.SuperService;

import java.util.List;

public interface StockViewService extends SuperService {

    List<Stock> getAllStock() throws Exception;

    List<StockTM> getAllStockForStockView() throws Exception;

    JasperPrint generateAllStockReport() throws Exception;
}
