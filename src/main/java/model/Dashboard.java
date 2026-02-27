package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {

    private double todayEarnings;
    private int todaySalesCount;
    private int lowStockCount;
    private int expiringCount;
}
