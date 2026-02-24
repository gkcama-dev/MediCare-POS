package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GRNItem {
    private int id;
    private double qty;
    private double buyingPrice;
    private int stockId;
    private long grnId;
}
