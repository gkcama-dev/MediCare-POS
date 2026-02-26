package service.custom;

import model.Invoice;
import model.tableModel.InvoiceViewTM;
import repository.SuperRepository;
import service.SuperService;

import java.util.List;

public interface InvoiceService extends SuperService {

    boolean placeInvoice(Invoice invoice) throws Exception;

    List<InvoiceViewTM> getAllInvoiceForView() throws Exception;
}
