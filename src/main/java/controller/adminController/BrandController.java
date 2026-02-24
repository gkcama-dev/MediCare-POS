package controller.adminController;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Brand;
import model.tableModel.BrandTM;
import model.tableModel.SupplierTM;
import service.ServiceFactory;
import service.custom.BrandService;
import util.ServiceType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class BrandController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TableColumn colBrand;

    @FXML
    private TableColumn colId;

    @FXML
    private TableView tblBrand;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtSearch;

    private final BrandService brandService = ServiceFactory.getInstance().getServiceType(ServiceType.BRAND);

    private ObservableList<BrandTM> brandMasterData = FXCollections.observableArrayList();

    private MedicineController medicineController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadBrandTable();
        setBrandID();
        addTableSelectionListener();
        initSearchListener();
    }


    public void setMedicineController(MedicineController controller) {
        this.medicineController = controller;
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void loadBrandTable() {
        Task<List<Brand>> loadTask = new Task<>() {
            @Override
            protected List<Brand> call() throws Exception {
                return brandService.getAllBrand();
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Brand> brands = loadTask.getValue();
            brandMasterData.clear();
            for (Brand b : brands) {
                brandMasterData.add(new BrandTM(b.getId(), b.getName()));
            }
            tblBrand.setItems(brandMasterData);
        });

        new Thread(loadTask).start();
    }

    private boolean isValid() {

        if (txtId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "ID Number is required!").show();
            return false;
        }

        if (txtBrand.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Brand Name is required!").show();
            return false;
        }
        return true;
    }

    private void clear() {
        txtId.clear();
        txtBrand.clear();
        btnAdd.setDisable(false);
        tblBrand.getSelectionModel().clearSelection();
    }


    private void addTableSelectionListener() {
        tblBrand.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields((BrandTM) newValue);
            }
        });
    }

    private void setBrandID() {
        try {
            txtId.setText(brandService.getBrandId());
            txtId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
            txtId.setDisable(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error !").show();
            e.printStackTrace();
        }
    }

    private void initSearchListener() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FilteredList<BrandTM> filteredData = new FilteredList<>(brandMasterData, b -> true);

            filteredData.setPredicate(brand -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filter = newValue.toLowerCase();

                return String.valueOf(brand.getId()).toLowerCase().contains(filter) ||
                        brand.getName().toLowerCase().contains(filter);
            });

            SortedList<BrandTM> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblBrand.comparatorProperty());

            tblBrand.setItems(sortedData);
        });
    }

    private void setDataToFields(BrandTM brandTM) {
        txtId.setText(String.valueOf(brandTM.getId()));
        txtBrand.setText(brandTM.getName());
        btnAdd.setDisable(true);
        txtId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        if (!isValid()) {
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String brandName = txtBrand.getText().trim();

        Brand brand = new Brand(id, brandName);

        try {
            if (brandService.addBrand(brand)) {
                new Alert(Alert.AlertType.INFORMATION, "Brand Added Successfully").show();
                loadBrandTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Brand  Added Failed").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
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

        int id = Integer.parseInt(txtId.getText());
        String brandName = txtBrand.getText().trim();

        Brand brand = new Brand(id, brandName);

        try {
            if (brandService.updateBrand(brand)) {
                new Alert(Alert.AlertType.INFORMATION, "Brand Updated Successfully!").show();
                loadBrandTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void tblBrandOnMouseClicked(MouseEvent event) {

        if (event.getClickCount() == 2) {
            BrandTM selectedBrand = (BrandTM) tblBrand.getSelectionModel().getSelectedItem();

            if (selectedBrand != null) {
                if (medicineController != null) {

                    medicineController.setBrandName(selectedBrand.getName());
                    Stage stage = (Stage) tblBrand.getScene().getWindow();
                    stage.close();
                } else {
                    System.err.println("MedicineController reference is null!");
                }
            }
        }
    }
}
