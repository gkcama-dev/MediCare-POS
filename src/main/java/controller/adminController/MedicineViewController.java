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
import model.Product;
import model.tableModel.ProductTM;
import service.ServiceFactory;
import service.custom.ProductService;
import util.ServiceType;
import util.listener.ProductSelectListener;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MedicineViewController implements Initializable {

    @FXML
    private TableColumn colBrand;

    @FXML
    private TableColumn colCategory;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colMobile;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableView tblMedicine;

    @FXML
    private TextField txtSearch;

    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

    private ObservableList<ProductTM> masterData = FXCollections.observableArrayList();

    private ProductSelectListener listener;

    public void setListener(ProductSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        loadTable();
        initSearchListener();
        initTableClickEvent();
    }


    private void initTableClickEvent() {
        tblMedicine.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleProductSelection();
            }
        });
    }

    private void handleProductSelection() {
        ProductTM selectedItem = (ProductTM) tblMedicine.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            if (listener != null) {
                listener.onProductSelect(selectedItem);
                closeWindow();
            } else {
                System.err.println("Listener is not set! Make sure to set the listener before opening this window.");
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tblMedicine.getScene().getWindow();
        stage.close();
    }

    private void setCellValues() {
        colId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void initSearchListener() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<ProductTM> filteredData = new FilteredList<>(masterData, p -> true);

            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filter = newValue.toLowerCase();

                return product.getName().toLowerCase().contains(filter) ||
                        product.getCode().toLowerCase().contains(filter) ||
                        product.getBrand().toLowerCase().contains(filter) ||
                        product.getCategory().toLowerCase().contains(filter) ||
                        product.getStatus().toLowerCase().contains(filter);
            });

            SortedList<ProductTM> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblMedicine.comparatorProperty());

            tblMedicine.setItems(sortedData);

        });
    }

    private void loadTable() {
        try {
            Task<List<Product>> loadTask = new Task<>() {
                @Override
                protected List<Product> call() throws Exception {

                    return productService.getAllProduct();
                }
            };

            loadTask.setOnSucceeded(event -> {
                List<Product> allProducts = loadTask.getValue();

                masterData.clear();

                for (Product product : allProducts) {
                    masterData.add(new ProductTM(
                            product.getCode(),
                            product.getName(),
                            product.getBrand(),
                            product.getCategory(),
                            product.getStatus()
                    ));
                }

                tblMedicine.setItems(masterData);
            });

            loadTask.setOnFailed(event -> {
                Throwable e = loadTask.getException();
                new Alert(Alert.AlertType.ERROR, "Error Loading Medicine Table: " + e.getMessage()).show();
            });

            new Thread(loadTask).start();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error Loading Table: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }
}

