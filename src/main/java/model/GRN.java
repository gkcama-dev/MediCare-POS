package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GRN {
    private long id;
    private LocalDate date;
    private double paidPrice;
    private double total;
    private double balance;
    private String supplierId;

    private List<GRNItem> grnItems;
    private List<Stock> stocks;
}
