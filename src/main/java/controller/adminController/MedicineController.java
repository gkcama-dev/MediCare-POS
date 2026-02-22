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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Product;
import model.Supplier;
import model.tableModel.CategoryTM;
import model.tableModel.ProductTM;
import model.tableModel.SupplierTM;
import service.ServiceFactory;
import service.custom.ProductService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MedicineController implements Initializable {

    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

    private ObservableList<ProductTM> masterData = FXCollections.observableArrayList();

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnBrand;

    @FXML
    private JFXButton btnCategory;

    @FXML
    private JFXComboBox cmbStatus;

    @FXML
    private TableColumn colBrand;

    @FXML
    private TableColumn colCategory;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableView tblMedicine;

    @FXML
    public TextField txtBrand;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        disableTextFields();
        loadTable();
        setProductID();
        loadStatusFromDb();
        initSearchListener();
        addTableSelectionListener();
    }

    public void setBrandName(String name) {
        txtBrand.setText(name);
        txtBrand.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    public void setCategoryName(String categoryName) {
        txtCategory.setText(categoryName);
        txtCategory.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    private void disableTextFields() {
        txtBrand.setDisable(true);
        txtCategory.setDisable(true);
    }

    private boolean isValid() {
        if (txtId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Product ID is required!").show();
            return false;
        }

        if (txtName.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Product Name is required!").show();
            txtName.requestFocus();
            return false;
        }

        if (txtBrand.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Brand is required!").show();
            txtBrand.requestFocus();
            return false;
        }

        if (txtCategory.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Category is required!").show();
            txtCategory.requestFocus();
            return false;
        }
        return true;
    }

    private void setProductID() {
        try {
            txtId.setText(productService.generateNextProductId());
            txtId.setEditable(false);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error !").show();
            e.printStackTrace();
        }
    }

    private void loadStatusFromDb() {
        try {
            List<String> status = productService.getProductStatus();
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

    private void setCellValueForTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("code"));
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

    private void addTableSelectionListener() {
        tblMedicine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields((ProductTM) newValue);
            }
        });
    }

    private void setDataToFields (ProductTM productTM) {
        txtId.setText(productTM.getCode());
        txtName.setText(productTM.getName());
        txtBrand.setText(productTM.getBrand());
        txtCategory.setText(productTM.getCategory());

        cmbStatus.setDisable(false);
        cmbStatus.setValue(productTM.getStatus());

        btnAdd.setDisable(true);
        txtBrand.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
        txtCategory.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
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

    private FXMLLoader loadWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); //like jDialog
            stage.setTitle(title);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            return loader;
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        txtBrand.clear();
        txtCategory.clear();
        cmbStatus.getSelectionModel().select("Active");
        cmbStatus.setDisable(true);
        tblMedicine.getSelectionModel().clearSelection();
        setProductID();
        txtName.requestFocus();
        btnAdd.setDisable(false);
    }

    private boolean isDataNotChanged(ProductTM tm) {
        return tm.getName().equals(txtName.getText().trim()) &&
                tm.getBrand().equals(txtBrand.getText().trim()) &&
                tm.getCategory().equals(txtCategory.getText().trim()) &&
                tm.getStatus().equals(cmbStatus.getValue().toString());
    }



    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!isValid()) {
            return;
        }

        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String category = txtCategory.getText().trim();
        String status = (cmbStatus.getSelectionModel().getSelectedItem() != null) ? cmbStatus.getSelectionModel().getSelectedItem().toString() : "Active";

        Product product = new Product(id, name, brand, category, status);

        try {
            if (productService.addProduct(product)) {
                new Alert(Alert.AlertType.INFORMATION, "Product Added Successfully").show();
                loadTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Product Added Failed").show();
            }
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
    void btnUpdateOnAction(ActionEvent event) {
        if (!isValid()) {
            return;
        }

        ProductTM selectedItem = (ProductTM) tblMedicine.getSelectionModel().getSelectedItem();

        if (isDataNotChanged(selectedItem)) {
            new Alert(Alert.AlertType.INFORMATION, "No changes detected to update!").show();
            return;
        }

        Product product = new Product(
                txtId.getText(),
                txtName.getText().trim(),
                txtBrand.getText().trim(),
                txtCategory.getText().trim(),
                cmbStatus.getValue().toString()
        );

        try {
            if (productService.updateProduct(product)) {
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
    void btnBrandOnAction(ActionEvent event) {
        FXMLLoader loader = loadWindow("/view/adminView/brand.fxml", "Brand Management");

        if (loader != null) {

            BrandController brandController = loader.getController();

            brandController.setMedicineController(this);
        }
    }

    @FXML
    void btnCategoryOnAction(ActionEvent event) {
        FXMLLoader loader = loadWindow("/view/adminView/category.fxml", "Category Management");

        if (loader != null) {
            CategoryController categoryController = loader.getController();
            categoryController.setMedicineController(this);
        }

    }

}


