package model.tableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserTM {
    private int id;
    private String username;
    private String password;
    private String userType;
    private String status;
}