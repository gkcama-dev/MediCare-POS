package controller.adminController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Brand;
import model.Category;
import model.tableModel.BrandTM;
import model.tableModel.CategoryTM;
import service.ServiceFactory;
import service.custom.CategoryService;
import util.ServiceType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    private final CategoryService  categoryService = ServiceFactory.getInstance().getServiceType(ServiceType.CATEGORY);

    private ObservableList<CategoryTM> categoryMasterData = FXCollections.observableArrayList();

    private MedicineController medicineController;

    @FXML
    private TableColumn colCategory;

    @FXML
    private TableColumn colId;

    @FXML
    private TableView tblCategory;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadCategoryTable();
    }

    public void setMedicineController(MedicineController controller) {
        this.medicineController = controller;
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void loadCategoryTable() {
        Task<List<Category>> loadTask = new Task<>() {
            @Override
            protected List<Category> call() throws Exception {
                return categoryService.getAllCategory();
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Category> categories = loadTask.getValue();
            categoryMasterData.clear();
            for (Category c : categories) {
                categoryMasterData.add(new CategoryTM(c.getId(), c.getName()));
            }
            tblCategory.setItems(categoryMasterData);
        });

        new Thread(loadTask).start();
    }

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
    void tblCategoryOnMouseClicked(MouseEvent event) {
        if(event.getClickCount() == 2){
            CategoryTM selectedCategory = (CategoryTM) tblCategory.getSelectionModel().getSelectedItem();

            if(selectedCategory != null){
                if (medicineController != null) {
                    medicineController.setCategoryName(selectedCategory.getName());

                    Stage stage = (Stage) tblCategory.getScene().getWindow();
                    stage.close();

                }else {
                    System.err.println("MedicineController reference is null!");
                }
            }
        }
    }
}
