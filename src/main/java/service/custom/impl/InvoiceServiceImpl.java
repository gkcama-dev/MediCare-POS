package service.custom.impl;

import database.DbConnection;
import model.Invoice;
import model.InvoiceItem;
import model.tableModel.InvoiceViewTM;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import repository.RepositoryFactory;
import repository.custom.InvoiceItemRepository;
import repository.custom.InvoiceRepository;
import repository.custom.StockRepository;
import service.custom.InvoiceService;
import util.RepositoryType;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // Save Items + Reduce Stock
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

    @Override
    public JasperPrint generateAllInvoiceReport() throws Exception {
        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/MedicarePOS-All-Invoice-Report.jrxml")
        );

        Connection connection = DbConnection.getInstance().getConnection();

        return JasperFillManager.fillReport(
                jasperReport,
                null,
                connection
        );
    }

    @Override
    public JasperPrint generateSingleInvoiceReport(Invoice invoice) throws Exception {
        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/MedicarePOS-Invoice-Bill.jrxml")
        );

        Connection connection = DbConnection.getInstance().getConnection();

        Map<String, Object> params = new HashMap<>();
        params.put("invoice_id", invoice.getId());
        params.put("Total", invoice.getTotal());
        params.put("Balance", invoice.getBalance());


        params.put("paid_amount", invoice.getPaidAmount());
        params.put("cutomer_mobile", invoice.getCustomerMobile());

        return JasperFillManager.fillReport(jasperReport, params, connection);
    }
}
