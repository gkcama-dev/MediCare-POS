package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockTM {
    private long grnId;
    private String supplier;
    private String product;
    private LocalDate mfd;
    private LocalDate exp;
    private double buyingPrice;
    private double sellingPrice;
    private double qty;
    private double total;
}
