package service.custom;

import model.Invoice;
import model.tableModel.InvoiceViewTM;
import net.sf.jasperreports.engine.JasperPrint;
import service.SuperService;

import java.util.List;

public interface InvoiceService extends SuperService {

    boolean placeInvoice(Invoice invoice) throws Exception;

    List<InvoiceViewTM> getAllInvoiceForView() throws Exception;

    JasperPrint generateAllInvoiceReport() throws Exception;

    JasperPrint generateSingleInvoiceReport(Invoice invoiceId) throws Exception;
}
