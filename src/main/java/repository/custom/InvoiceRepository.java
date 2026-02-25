package repository.custom;

import model.Invoice;
import repository.SuperRepository;

import java.sql.Connection;

public interface InvoiceRepository extends SuperRepository {
    boolean save(Invoice invoice, Connection connection) throws Exception;
    long getNextInvoiceId() throws Exception;
}
