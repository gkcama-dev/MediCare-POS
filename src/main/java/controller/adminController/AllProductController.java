package controller.adminController;

import com.jfoenix.controls.JFXButton;
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
import model.Product;
import model.tableModel.ProductTM;
import service.ServiceFactory;
import service.custom.ProductService;
import util.ServiceType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AllProductController implements Initializable {

    @FXML
    private JFXButton btnPrint;

    @FXML
    private TableColumn<ProductTM, String> colBrand;

    @FXML
    private TableColumn<ProductTM, String> colCategory;

    @FXML
    private TableColumn<ProductTM, String> colCode;

    @FXML
    private TableColumn<ProductTM, String> colName;

    @FXML
    private TableColumn<ProductTM, String> colStatus;

    @FXML
    private TableView<ProductTM> tblProduct;

    @FXML
    private TextField txtSearch;

    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

    private ObservableList<ProductTM> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadTable();
        setStatusColor();
        initSearchListener();
    }

    private void setCellValueFactory() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
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

                tblProduct.setItems(masterData);

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

    private void setStatusColor() {

        colStatus.setCellFactory(column -> new TableCell<ProductTM, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);

                    if (status.equalsIgnoreCase("Active")) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });
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
            sortedData.comparatorProperty().bind(tblProduct.comparatorProperty());

            tblProduct.setItems(sortedData);

        });
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }
}
