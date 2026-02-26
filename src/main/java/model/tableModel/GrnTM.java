package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrnTM {
    private long id;
    private LocalDate date;
    private double total;
    private double paidAmount;
    private double balance;
    private String supplier;
    private double qty;
}
