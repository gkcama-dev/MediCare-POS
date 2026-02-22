package controller.adminController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Supplier;
import model.tableModel.SupplierTM;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    private ObservableList<SupplierTM> masterData = FXCollections.observableArrayList();

    @FXML
    private JFXButton btnAdd;

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
        setCellValueForTable();
        loadTable();
        setSupplierID();
        loadStatusFromDb();
        addTableSelectionListener();
        initSearchListener();
        setMobileFieldValidation();
    }

    private void setSupplierID() {
        try {
            txtId.setText(supplierService.generateNextSupplierId());
            txtId.setEditable(false);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error !").show();
            e.printStackTrace();
        }
    }

    private void loadStatusFromDb() {
        try {
            List<String> status = supplierService.getSupplierStatus();
            ObservableList<String> obList = FXCollections.observableArrayList(status);
            cmbStatus.setItems(obList);

            if (!obList.isEmpty()) {
                cmbStatus.getSelectionModel().select("Active");
                cmbStatus.setDisable(true);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Status Load Error !").show();
            throw new RuntimeException(e);
        }

    }

    private boolean isValid() {
        if (txtId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Supplier ID is required!").show();
            return false;
        }

        if (txtFirstName.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "First Name is required!").show();
            txtFirstName.requestFocus();
            return false;
        }

        if (txtLastName.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Last Name is required!").show();
            txtLastName.requestFocus();
            return false;
        }

        if (txtEmail.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Company is required!").show();
            txtLastName.requestFocus();
            return false;
        }

        if (txtMobile.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Mobile number is required!").show();
            txtMobile.requestFocus();
            return false;
        } else if (!txtMobile.getText().matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Invalid Mobile! Please enter 10 digits.").show();
            txtMobile.requestFocus();
            return false;
        }

        if (txtEmail.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Email is required!").show();
            txtEmail.requestFocus();
            return false;
        } else if (!txtEmail.getText().contains("@")) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email! '@' symbol is required.").show();
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void addTableSelectionListener() {
        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields((SupplierTM) newValue);
            }
        });
    }

    private void setDataToFields(SupplierTM supplierTM) {
        txtId.setText(supplierTM.getId());
        txtFirstName.setText(supplierTM.getFirstName());
        txtLastName.setText(supplierTM.getLastName());
        txtCompany.setText(supplierTM.getCompany());
        txtMobile.setText(supplierTM.getMobile());
        txtEmail.setText(supplierTM.getEmail());
        cmbStatus.setDisable(false);
        cmbStatus.setValue(supplierTM.getStatus());

        btnAdd.setDisable(true);
    }

    private void clear() {
        txtId.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtCompany.clear();
        txtMobile.clear();
        txtEmail.clear();
        cmbStatus.getSelectionModel().select("Active");
        cmbStatus.setDisable(true);
        tblSuppliers.getSelectionModel().clearSelection();
        setSupplierID();
        txtFirstName.requestFocus();
    }

    private boolean isDataNotChanged(SupplierTM tm) {
        return tm.getFirstName().equals(txtFirstName.getText().trim()) &&
                tm.getLastName().equals(txtLastName.getText().trim()) &&
                tm.getCompany().equals(txtCompany.getText().trim()) &&
                tm.getMobile().equals(txtMobile.getText().trim()) &&
                tm.getEmail().equals(txtEmail.getText().trim()) &&
                tm.getStatus().equals(cmbStatus.getValue().toString());
    }

    private void initSearchListener() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FilteredList<SupplierTM> filteredData = new FilteredList<>(masterData, p -> true);

            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filter = newValue.toLowerCase();

                return supplier.getFirstName().toLowerCase().contains(filter) ||
                        supplier.getLastName().toLowerCase().contains(filter) ||
                        supplier.getCompany().toLowerCase().contains(filter) ||
                        supplier.getMobile().toLowerCase().contains(filter) ||
                        supplier.getEmail().toLowerCase().contains(filter) ||
                        supplier.getId().toLowerCase().contains(filter);
            });

            SortedList<SupplierTM> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblSuppliers.comparatorProperty());

            tblSuppliers.setItems(sortedData);

        });
    }

    private void loadTable() {

        Task<List<Supplier>> loadTask = new Task<>() {
            @Override
            protected List<Supplier> call() throws Exception {
                return supplierService.getAllSupplier();
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Supplier> allSupplier = loadTask.getValue();

            masterData.clear();

            for (Supplier supplier : allSupplier) {
                masterData.add(new SupplierTM(
                        supplier.getId(),
                        supplier.getFirstName(),
                        supplier.getLastName(),
                        supplier.getCompany(),
                        supplier.getMobile(),
                        supplier.getEmail(),
                        supplier.getStatus()
                ));
            }
            tblSuppliers.setItems(masterData);
        });

        loadTask.setOnFailed(event -> {
            Throwable e = loadTask.getException();
            new Alert(Alert.AlertType.ERROR, "Error Loading Table: " + e.getMessage()).show();
        });

        new Thread(loadTask).start();
//        try {
//            List<Supplier> allSupplier = supplierService.getAllSupplier();
//
//
//            ArrayList<SupplierTM> suppliersList = new ArrayList<>();
//
//            allSupplier.forEach(supplier -> {
//                suppliersList.add(new SupplierTM(
//                        supplier.getId(),
//                        supplier.getFirstName(),
//                        supplier.getLastName(),
//                        supplier.getCompany(),
//                        supplier.getMobile(),
//                        supplier.getEmail(),
//                        supplier.getStatus()
//                ));
//            });
//            tblSuppliers.setItems(FXCollections.observableArrayList(suppliersList));
//        } catch (SQLException e) {
//            new Alert(Alert.AlertType.ERROR, "Error Loading Data to Table" + e.getMessage()).show();
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            new Alert(Alert.AlertType.ERROR, "Error Loading Data to Table" + e.getMessage()).show();
//            throw new RuntimeException(e);
//        }
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setMobileFieldValidation() {
        txtMobile.textProperty().addListener((observable, oldValue, newValue) -> {
           if(!newValue.matches("\\d*")){
               txtMobile.setText(oldValue);
           }

            if (txtMobile.getText().length() > 10) {
                String s = txtMobile.getText().substring(0, 10);
                txtMobile.setText(s);
            }
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        if (!isValid()) {
            return;
        }

        String id = txtId.getText();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String company = txtCompany.getText().trim();
        String mobile = txtMobile.getText().trim();
        String email = txtEmail.getText().trim();
        String status = (cmbStatus.getSelectionModel().getSelectedItem() != null) ? cmbStatus.getSelectionModel().getSelectedItem().toString() : "Active";

        Supplier supplier = new Supplier(id, firstName, lastName, company, mobile, email, status);

        try {
            if (supplierService.addSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Added Successfully").show();
                loadTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Supplier Added Failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!isValid()) {
            return;
        }

        SupplierTM selectedItem = (SupplierTM) tblSuppliers.getSelectionModel().getSelectedItem();

        if (isDataNotChanged(selectedItem)) {
            new Alert(Alert.AlertType.INFORMATION, "No changes detected to update!").show();
            return;
        }

        Supplier supplier = new Supplier(
                txtId.getText(),
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                txtCompany.getText().trim(),
                txtMobile.getText().trim(),
                txtEmail.getText().trim(),
                cmbStatus.getValue().toString()
        );

        try {
            if (supplierService.updateSupplier(supplier)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                loadTable();
                clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();

    }

    @FXML
    void btnSearchClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

}
