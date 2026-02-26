package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceViewTM {

    private long id;
    private LocalDate date;
    private String customer;
    private double discount;
    private String paymentType;
    private double total;
    private String user;

}
