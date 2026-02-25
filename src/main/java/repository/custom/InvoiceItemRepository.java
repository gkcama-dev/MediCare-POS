package repository.custom;

import model.InvoiceItem;
import repository.SuperRepository;

import java.sql.Connection;

public interface InvoiceItemRepository extends SuperRepository {
    boolean save(InvoiceItem item, Connection connection) throws Exception;
}
