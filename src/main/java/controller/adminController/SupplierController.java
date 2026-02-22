package controller.adminController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private JFXButton btnAddOnAction;

    @FXML
    private JFXButton btnClearOnAction;

    @FXML
    private JFXButton btnSearchClearOnAction;

    @FXML
    private JFXButton btnSearchOnAction;

    @FXML
    private JFXButton btnUpdateOnAction;

    @FXML
    private JFXComboBox cmbStatus;

    @FXML
    private TableColumn colCompany;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colFirstName;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colLastName;

    @FXML
    private TableColumn colMobile;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableView tblSuppliers;

    @FXML
    private TextField txtCompany;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         setSupplierID();
    }

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    private void setSupplierID(){
        try {
            txtId.setText(supplierService.generateNextSupplierId());
            txtId.setEditable(false);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error").show();
            e.printStackTrace();
        }
    }
}
