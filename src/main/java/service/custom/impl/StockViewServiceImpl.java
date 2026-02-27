package service.custom.impl;

import database.DbConnection;
import model.Stock;
import model.tableModel.StockTM;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import repository.RepositoryFactory;
import repository.custom.StockRepository;
import service.custom.StockViewService;
import util.RepositoryType;

import java.sql.Connection;
import java.util.List;

public class StockViewServiceImpl implements StockViewService {

    private final StockRepository stockRepository =
            RepositoryFactory.getInstance().getRepository(RepositoryType.STOCK);

    @Override
    public List<Stock> getAllStock() throws Exception {
        return stockRepository.getAllStock();
    }

    @Override
    public List<StockTM> getAllStockForStockView() throws Exception {
        return stockRepository.getAllStockForStockView();
    }

    @Override
    public JasperPrint generateAllStockReport() throws Exception {
        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/MedicarePOS-All-Stock-Report.jrxml")
        );

        Connection connection = DbConnection.getInstance().getConnection();

        return JasperFillManager.fillReport(
                jasperReport,
                null,
                connection
        );
    }


}
