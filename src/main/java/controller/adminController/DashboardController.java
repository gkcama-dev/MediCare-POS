package controller.adminController;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNode("/view/adminView/home.fxml",btnDashboard);

    }

    @FXML
    private JFXButton btnAllInvoice;

    @FXML
    private JFXButton btnAllProducts;

    @FXML
    private JFXButton btnAllStocks;

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnMedicine;

    @FXML
    private JFXButton btnStock;

    @FXML
    private JFXButton btnSupplier;

    @FXML
    private JFXButton btnUser;

    @FXML
    private AnchorPane dashroot;

    @FXML
    void btnAllInvoiceOnAction(ActionEvent event) {
       setNode("/view/adminView/allInvoice.fxml",btnAllInvoice);
    }

    @FXML
    void btnAllProductsOnAction(ActionEvent event) {
       setNode("/view/adminView/allProduct.fxml",btnAllProducts);
    }

    @FXML
    void btnAllStockOnAction(ActionEvent event) {
       setNode("/view/adminView/allStock.fxml",btnAllStocks);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {

        setNode("/view/adminView/home.fxml",btnDashboard);
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnMedicineOnAction(ActionEvent event) {
         setNode("/view/adminView/medicine.fxml",btnMedicine);
    }

    @FXML
    void btnStockOnAction(ActionEvent event) {
        setNode("/view/adminView/stock.fxml",btnStock);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        setNode("/view/adminView/supplier.fxml",btnSupplier);
    }

    @FXML
    void btnUserOnAction(ActionEvent event) {
         setNode("/view/adminView/user.fxml",btnUser);
    }

    private void setNode(String fxmlPath ,JFXButton selectedBtn) {
        //sidebar active button
        updateSidebarActiveState(selectedBtn);

        URL resource = getClass().getResource(fxmlPath);
        // Validation
        if (resource == null) {
            System.err.println("Error: FXML file not found at " + fxmlPath);
            //Alert
            return;
        }
        try {
            Parent parent = FXMLLoader.load(resource);
            dashroot.getChildren().clear();
            dashroot.getChildren().add(parent);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void updateSidebarActiveState(JFXButton activeBtn) {

        JFXButton[] buttons = {btnDashboard, btnSupplier, btnMedicine, btnStock, btnUser, btnAllStocks, btnAllInvoice,btnAllProducts};

        for (JFXButton btn : buttons) {
            if (btn != null) {
                btn.getStyleClass().remove("active");
            }
        }

        if (activeBtn != null) {
            activeBtn.getStyleClass().add("active");
        }
    }



}
