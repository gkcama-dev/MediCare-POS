package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockViewTM {
    private int id;
    private String medicine;
    private double price;
    private double stockQty;
}
