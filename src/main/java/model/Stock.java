package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Stock {
    private int id;
    private String productCode;
    private double sellingPrice;
    private double qty;
    private LocalDate mfd;
    private LocalDate exp;
    private int userId;

}
