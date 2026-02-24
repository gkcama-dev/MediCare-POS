package service.custom.impl;

import database.DbConnection;
import model.GRN;
import model.GRNItem;
import model.Stock;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import repository.custom.GRNItemRepository;
import repository.custom.GRNRepository;
import repository.custom.StockRepository;
import repository.custom.impl.GRNItemRepositoryImpl;
import repository.custom.impl.StockRepositoryImpl;
import service.custom.GRNService;
import util.RepositoryType;

import java.sql.Connection;
import java.util.List;

public class GRNServiceImpl implements GRNService {

    private final GRNRepository grnRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.GRN);
    private final GRNItemRepository grnItemRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.GRN_ITEM);
    private final StockRepository stockRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.STOCK);

    @Override
    public boolean placeGRN(GRN grn) throws Exception {

        Connection connection = DbConnection.getInstance().getConnection();

        try {
            // Start Transaction
            connection.setAutoCommit(false);

            // Save GRN Master Record
            if (grnRepository.save(grn, connection)) {

                //Process each stock item in the GRN
                for (int i = 0; i < grn.getGrnItems().size(); i++) {

                    Stock stock = grn.getStocks().get(i);
                    GRNItem item = grn.getGrnItems().get(i);

                    // Check if the exact same stock batch already exists
                    int existingStockId = stockRepository.isExists(stock, connection);
                    int finalStockId;

                    if (existingStockId != -1) {
                        // If stock exists, update the quantity of the current batch
                        stockRepository.updateQty(existingStockId, stock.getQty(), connection);
                        finalStockId = existingStockId;
                    } else {
                        // If it's a new batch, save it and get the new Stock ID
                        finalStockId = stockRepository.saveAndGetId(stock, connection);
                    }

                     // Save the GRN Detail (Item) record linked to the Stock ID
                    if (finalStockId > 0) {
                        item.setStockId(finalStockId);
                        item.setGrnId(grn.getId());
                        if (!grnItemRepository.save(item, connection)) {
                            connection.rollback();
                            return false;
                        }
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
                connection.commit(); // Commit transaction if all steps are successful
                return true;
            }
            // Rollback if GRN master record failed to save
            connection.rollback();
            return false;
        } catch (Exception e) {
            // Rollback on any unexpected exception
            connection.rollback();
            throw e;
        } finally {
            // Reset connection auto-commit mode
            connection.setAutoCommit(true);
        }
    }
}
