package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceItem {

    private int id;
    private double qty;
    private double unitPrice;
    private long invoiceId;
    private int stockId;
}
