package controller.adminController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Brand;
import model.tableModel.BrandTM;
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

    private final BrandService brandService = ServiceFactory.getInstance().getServiceType(ServiceType.BRAND);

    private ObservableList<BrandTM> brandMasterData = FXCollections.observableArrayList();

    private MedicineController medicineController;

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


    @FXML
    void btnAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void tblBrandOnMouseClicked(MouseEvent event) {

        if(event.getClickCount() == 2){
            BrandTM selectedBrand = (BrandTM) tblBrand.getSelectionModel().getSelectedItem();

            if(selectedBrand != null){
                if (medicineController != null) {

                    medicineController.setBrandName(selectedBrand.getName());
                    Stage stage = (Stage) tblBrand.getScene().getWindow();
                    stage.close();
                }else {
                    System.err.println("MedicineController reference is null!");
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadBrandTable();
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
}
