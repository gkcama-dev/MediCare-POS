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
import javafx.scene.control.Alert;
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
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(ServiceType.CATEGORY);

    private ObservableList<CategoryTM> categoryMasterData = FXCollections.observableArrayList();

    private MedicineController medicineController;

    @FXML
    private JFXButton btnAdd;

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
        setCategoryID();
        addTableSelectionListener();
        initSearchListener();
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

    private void clear() {
        txtId.clear();
        txtCategory.clear();
        btnAdd.setDisable(false);
        tblCategory.getSelectionModel().clearSelection();
    }

    private void setCategoryID() {
        try {
            txtId.setText(categoryService.getCategoryId());
            txtId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
            txtId.setDisable(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error !").show();
            e.printStackTrace();
        }
    }

    private void addTableSelectionListener() {
        tblCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields((CategoryTM) newValue);
            }
        });
    }

    private void setDataToFields(CategoryTM categoryTM) {
        txtId.setText(String.valueOf(categoryTM.getId()));
        txtCategory.setText(categoryTM.getName());
        btnAdd.setDisable(true);
        txtId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    private void initSearchListener() {

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FilteredList<CategoryTM> filteredData = new FilteredList<>(categoryMasterData, c -> true);

            filteredData.setPredicate(category -> {

                if (newValue == null || newValue.isEmpty()) return true;
                String filter = newValue.toLowerCase();

                return String.valueOf(category.getId()).toLowerCase().contains(filter) ||
                        category.getName().toLowerCase().contains(filter);

            });
            SortedList<CategoryTM> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblCategory.comparatorProperty());
            tblCategory.setItems(sortedData);
        });
    }

    private boolean isValid() {

        if (txtId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "ID Number is required!").show();
            return false;
        }

        if (txtCategory.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Category Name is required!").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        if (!isValid()) {
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String categoryName = txtCategory.getText().trim();

        Category category = new Category(id, categoryName);

        try {
            if (categoryService.addCategory(category)) {
                new Alert(Alert.AlertType.INFORMATION, "Category Added Successfully").show();
                loadCategoryTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Category Added Failed").show();
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
        String categoryName = txtCategory.getText().trim();

        Category category = new Category(id, categoryName);

        try {
            if (categoryService.updateCategory(category)) {
                new Alert(Alert.AlertType.INFORMATION, "Category Updated Successfully!").show();
                loadCategoryTable();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Category Update Failed!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void tblCategoryOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            CategoryTM selectedCategory = (CategoryTM) tblCategory.getSelectionModel().getSelectedItem();

            if (selectedCategory != null) {
                if (medicineController != null) {
                    medicineController.setCategoryName(selectedCategory.getName());

                    Stage stage = (Stage) tblCategory.getScene().getWindow();
                    stage.close();

                } else {
                    System.err.println("MedicineController reference is null!");
                }
            }
        }
    }
}
