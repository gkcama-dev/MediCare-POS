package controller.adminController;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.GRN;
import model.GRNItem;
import model.Stock;
import model.tableModel.StockTM;
import service.ServiceFactory;
import service.custom.GRNService;
import util.ServiceType;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StockController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnSaveGRN;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colBuyPrice;

    @FXML
    private TableColumn colEXP;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colMFD;

    @FXML
    private TableColumn colMedicine;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colSellPrice;

    @FXML
    private TableColumn colSupplier;

    @FXML
    private DatePicker dateEXP;

    @FXML
    private DatePicker dateMFD;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView tblStock;

    @FXML
    private TextField txtBuyingPrice;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtMedicine;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSellingPrice;

    @FXML
    private TextField txtSupplier;

    @FXML
    private JFXButton btnMedicine;

    @FXML
    private JFXButton btnSupplier;

    private final GRNService grnService = ServiceFactory.getInstance().getServiceType(ServiceType.GRN);

    private ObservableList<StockTM> stockDetailsList = FXCollections.observableArrayList();

    private String selectedSupplierId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGRNId();
        setCellValueForTable();
        initTableDoubleClickEvent();
        disableField();
        dateValidate();
    }

    private long generateRandomGrnId() {

        long timeStamp = System.currentTimeMillis();
        int randomPart = (int) (Math.random() * 900) + 100;
        return Long.parseLong(String.valueOf(timeStamp).substring(4) + randomPart);

    }

    private void disableField() {
        dateEXP.setEditable(false);
        dateMFD.setEditable(false);
        txtSupplier.setEditable(false);
        txtMedicine.setEditable(false);
    }

    private void setGRNId() {
        long randomGrnId = generateRandomGrnId();
        txtId.setText(String.valueOf(randomGrnId));
        txtId.setDisable(true);
        txtId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    private void dateValidate() {
        dateEXP.setOnAction(event -> {
            LocalDate mfd = dateMFD.getValue();
            LocalDate exp = dateEXP.getValue();

            if (mfd != null && exp != null) {
                if (exp.isBefore(mfd) || exp.isEqual(mfd)) {
                    new Alert(Alert.AlertType.ERROR, "Expiry date must be after the Manufacturing date!").show();
                    dateEXP.setValue(null);
                }
            }
        });
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("grnId"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("product"));
        colMFD.setCellValueFactory(new PropertyValueFactory<>("mfd"));
        colEXP.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblStock.setItems(stockDetailsList);
    }

    private void initTableDoubleClickEvent() {
        tblStock.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StockTM selectedItem = (StockTM) tblStock.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to remove this item (" + selectedItem.getProduct() + ")?",
                            ButtonType.YES, ButtonType.NO);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        stockDetailsList.remove(selectedItem);

                        calculateNetTotal();

                        tblStock.refresh();
                    }
                }
            }
        });
    }

    private boolean isValid() {

        if (txtSupplier.getText().trim().isEmpty() ||
                txtMedicine.getText().trim().isEmpty() ||
                txtBuyingPrice.getText().trim().isEmpty() ||
                txtSellingPrice.getText().trim().isEmpty() ||
                txtQty.getText().trim().isEmpty() ||
                dateMFD.getValue() == null ||
                dateEXP.getValue() == null) {

            new Alert(Alert.AlertType.WARNING, "All fields are required!").show();
            return false;
        }

        try {

            double buyPrice = Double.parseDouble(txtBuyingPrice.getText());
            double sellPrice = Double.parseDouble(txtSellingPrice.getText());
            double qty = Double.parseDouble(txtQty.getText());


            if (buyPrice <= 0 || sellPrice <= 0 || qty <= 0) {
                new Alert(Alert.AlertType.ERROR, "Price and Quantity must be greater than zero!").show();
                return false;
            }


            if (sellPrice < buyPrice) {
                new Alert(Alert.AlertType.ERROR, "Selling price cannot be less than the buying price!").show();
                return false;
            }


            if (dateMFD.getValue() == null || dateEXP.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please select MFD and EXP dates!").show();
                return false;
            }


        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input! Please enter numeric values for prices and quantity.").show();
        }
        return true;
    }



@FXML
void btnAddOnAction(ActionEvent event) {

    if (!isValid()) {
        return;
    }

    try {

        long GRNId = Long.parseLong(txtId.getText());
        String supplier = txtSupplier.getText();
        String medicine = txtMedicine.getText();
        LocalDate mfd = dateMFD.getValue();
        LocalDate exp = dateEXP.getValue();
        double buyPrice = Double.parseDouble(txtBuyingPrice.getText());
        double sellPrice = Double.parseDouble(txtSellingPrice.getText());
        double qty = Double.parseDouble(txtQty.getText());

        if (buyPrice <= 0 || sellPrice <= 0 || qty <= 0) {
            new Alert(Alert.AlertType.ERROR, "Price and Quantity must be greater than zero!").show();
            return;
        }

        if (sellPrice < buyPrice) {
            new Alert(Alert.AlertType.ERROR, "Selling price cannot be less than the buying price!").show();
            return;
        }

        if (dateMFD.getValue() == null || dateEXP.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select MFD and EXP dates!").show();
            return;
        }

        for (StockTM tm : stockDetailsList) {

            if (tm.getSupplier().equals(supplier) && tm.getProduct().equals(medicine) &&
                    tm.getBuyingPrice() == buyPrice && tm.getSellingPrice() == sellPrice &&
                    tm.getExp().equals(exp) && tm.getMfd().equals(mfd)) {

                tm.setQty(tm.getQty() + qty);
                tm.setTotal(tm.getQty() * tm.getBuyingPrice());

                tblStock.refresh();
                calculateNetTotal();
                clearItemFields();
                return;
            }
        }

        double total = qty * buyPrice;
        StockTM stockTM = new StockTM(
                GRNId,
                supplier,
                medicine,
                mfd,
                exp,
                buyPrice,
                sellPrice,
                qty,
                total
        );

        stockDetailsList.add(stockTM);
        tblStock.setItems(stockDetailsList);

        calculateNetTotal();
        clearItemFields();

    } catch (Exception e) {
        new Alert(Alert.AlertType.ERROR, "Please enter prices and quantities correctly.!").show();
    }
}

private void calculateNetTotal() {
    double netTotal = 0.0;
    for (StockTM tm : stockDetailsList) {
        netTotal += tm.getTotal();
    }
    lblTotal.setText(String.format("%.2f", netTotal));
}

private void clearItemFields() {
    txtMedicine.clear();
    txtSupplier.clear();
    txtBuyingPrice.clear();
    txtSellingPrice.clear();
    txtQty.clear();
    dateEXP.setValue(null);
    dateMFD.setValue(null);
}

@FXML
void btnClearOnAction(ActionEvent event) {

}

@FXML
void btnSaveGRNOnAction(ActionEvent event) {

    if (stockDetailsList.isEmpty()) {
        new Alert(Alert.AlertType.WARNING, "Please add items to the table!").show();
        return;
    }

    List<GRNItem> grnItemsList = new ArrayList<>();
    List<Stock> stocksList = new ArrayList<>();

    for (StockTM tm : stockDetailsList) {
        stocksList.add(new Stock(
                0,
                tm.getProduct(),
                tm.getSellingPrice(),
                tm.getQty(),
                tm.getMfd(),
                tm.getExp(),
                1
        ));

        grnItemsList.add(new GRNItem(
                0,
                tm.getQty(),
                tm.getBuyingPrice(),
                0,
                tm.getGrnId()
        ));
    }

    GRN grn = new GRN(
            Long.parseLong(txtId.getText()),
            LocalDate.now(),
            0.0,
            Double.parseDouble(lblTotal.getText()),
            0.0,
            selectedSupplierId,
            grnItemsList,
            stocksList
    );

    try {

        if (grnService.placeGRN(grn)) {
            new Alert(Alert.AlertType.INFORMATION, "GRN Saved Successfully!").show();
            stockDetailsList.clear();
            tblStock.refresh();
            setGRNId();
            calculateNetTotal();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save GRN!").show();
        }

    } catch (Exception e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        throw new RuntimeException(e);
    }


}

@FXML
void btnMedicineOnAction(ActionEvent event) {
    loadWindow("/view/adminView/medicineView.fxml", "MedicineView");
}

@FXML
void btnSupplierOnAction(ActionEvent event) {
    loadWindow("/view/adminView/supplierView.fxml", "SuppllierView");
}


private void loadWindow(String fxmlPath, String title) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Object controller = loader.getController();


        if (controller instanceof MedicineViewController) {
            ((MedicineViewController) controller).setListener(product -> {

                if (!product.getStatus().equalsIgnoreCase("Active")) {
                    new Alert(Alert.AlertType.ERROR,
                            "Selected medicine is not Active!").show();
                    return;
                }


                txtMedicine.setText(product.getCode());
            });
        } else if (controller instanceof SupplierViewController) {
            ((SupplierViewController) controller).setListener(supplier -> {

                if (!supplier.getStatus().equalsIgnoreCase("Active")) {
                    new Alert(Alert.AlertType.ERROR,
                            "Selected supplier is not Active!").show();
                    return;
                }

                txtSupplier.setText(supplier.getMobile());
                this.selectedSupplierId = supplier.getId();
            });
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
