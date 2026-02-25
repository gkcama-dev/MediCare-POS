package model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invoice {

    private long id;
    private LocalDate date;
    private String customerMobile;
    private double paidAmount;
    private double discount;
    private double balance;
    private double total;
    private int userId;
    private int paymentMethodId;

    private List<InvoiceItem> items;

}
