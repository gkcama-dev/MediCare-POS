package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierTM {
    private String id;
    private String firstName;
    private String lastName;
    private String company;
    private String mobile;
    private String email;
    private String status;
}
