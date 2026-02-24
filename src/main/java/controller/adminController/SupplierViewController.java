package controller.adminController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Supplier;
import model.tableModel.ProductTM;
import model.tableModel.SupplierTM;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;
import util.listener.SupplierSelectListener;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierViewController implements Initializable {

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
    private TableView tblSupplier;

    @FXML
    private TextField txtSearch;

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    private ObservableList<SupplierTM> masterData = FXCollections.observableArrayList();

    private SupplierSelectListener listener;

    public void setListener(SupplierSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadTable();
        initSearchListener();
        initTableClickEvent();
    }

    private void initTableClickEvent() {
        tblSupplier.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleSupplierSelection();
            }
        });
    }

    private void handleSupplierSelection() {
        SupplierTM selectedItem = (SupplierTM) tblSupplier.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            if (listener != null) {
                listener.onSupplierSelect(selectedItem);
                closeWindow();
            } else {
                System.err.println("Listener is not set! Make sure to set the listener before opening this window.");
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tblSupplier.getScene().getWindow();
        stage.close();
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
            tblSupplier.setItems(masterData);
        });

        loadTask.setOnFailed(event -> {
            Throwable e = loadTask.getException();
            new Alert(Alert.AlertType.ERROR, "Error Loading Table: " + e.getMessage()).show();
        });

        new Thread(loadTask).start();
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
            sortedData.comparatorProperty().bind(tblSupplier.comparatorProperty());

            tblSupplier.setItems(sortedData);

        });
    }

}

