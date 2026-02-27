package service.custom.impl;

import database.DbConnection;
import model.Invoice;
import model.InvoiceItem;
import model.tableModel.InvoiceViewTM;
import repository.RepositoryFactory;
import repository.custom.InvoiceItemRepository;
import repository.custom.InvoiceRepository;
import repository.custom.StockRepository;
import service.custom.InvoiceService;
import util.RepositoryType;

import java.sql.Connection;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.INVOICE);
    private final InvoiceItemRepository invoiceItemRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.INVOICE_ITEM);
    private final StockRepository stockRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.STOCK);

    @Override
    public boolean placeInvoice(Invoice invoice) throws Exception {

        Connection connection = DbConnection.getInstance().getConnection();

        try {

            connection.setAutoCommit(false); //Start Transaction

            if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
                connection.rollback();
                return false;
            }

            //Save Invoice
            boolean invoiceSaved = invoiceRepository.save(invoice, connection);

            if (!invoiceSaved) {
                connection.rollback();
                return false;
            }

            // 2️⃣ Save Items + Reduce Stock
            for (InvoiceItem item : invoice.getItems()) {

                boolean itemSaved = invoiceItemRepository.save(item, connection);

                if (!itemSaved) {
                    connection.rollback();
                    return false;
                }

                boolean stockUpdated = stockRepository.reduceQty(
                        item.getStockId(),
                        item.getQty(),
                        connection
                );

                if (!stockUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (Exception e) {

            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
        }

    }

    @Override
    public List<InvoiceViewTM> getAllInvoiceForView() throws Exception {
        return invoiceRepository.getAllInvoiceForView();
    }
}
