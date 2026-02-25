package service.custom;

import model.Invoice;
import repository.SuperRepository;
import service.SuperService;

public interface InvoiceService extends SuperService {
    boolean placeInvoice(Invoice invoice) throws Exception;
}
