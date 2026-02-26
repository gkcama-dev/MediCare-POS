package repository.custom;

import model.Invoice;
import model.tableModel.InvoiceViewTM;
import repository.SuperRepository;

import java.sql.Connection;
import java.util.List;

public interface InvoiceRepository extends SuperRepository {
    boolean save(Invoice invoice, Connection connection) throws Exception;

    long getNextInvoiceId() throws Exception;

    List<InvoiceViewTM> getAllInvoiceForView() throws Exception;
}
