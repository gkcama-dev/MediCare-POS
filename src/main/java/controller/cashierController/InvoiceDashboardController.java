package controller.cashierController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Invoice;
import model.InvoiceItem;
import model.tableModel.InvoiceTM;
import service.ServiceFactory;
import service.SuperService;
import service.custom.InvoiceService;
import service.custom.impl.InvoiceServiceImpl;
import util.ServiceType;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InvoiceDashboardController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnStock;

    @FXML
    private JFXComboBox cmdPaymentMethod;

    @FXML
    private TableColumn colBrand;

    @FXML
    private TableColumn colMedicine;

    @FXML
    private TableColumn colPrice;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colStockID;

    @FXML
    private TableColumn colTotal;

    @FXML
    private AnchorPane dashroot;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView tblInvoice;

    @FXML
    private TextField txtAvbQty;

    @FXML
    private TextField txtProduct;

    @FXML
    private TextField txtCutomerMobile;

    @FXML
    private TextField txtInvoiceBalance;

    @FXML
    private TextField txtInvoiceDiscount;

    @FXML
    private TextField txtInvoiceID;

    @FXML
    private TextField txtInvoicePayment;

    @FXML
    private TextField txtInvoiceTotal;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtStockID;

    private final InvoiceService invoiceService =
            ServiceFactory.getInstance().getServiceType(ServiceType.INVOICE);

    private ObservableList<InvoiceTM> invoiceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        setInvoiceId();
        tblInvoice.setItems(invoiceList);
        tblInvoice.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        removeRaw();
        calculateTotalLabel();
        calculateNetTotal();
        disableFiled();
        allowOnlyNumbers(txtQty);
        allowOnlyNumbers(txtInvoicePayment);
        allowOnlyNumbers(txtCutomerMobile);
        txtInvoicePayment.textProperty().addListener((obs, oldVal, newVal) -> calculateFinalAmounts());
    }

    private void setInvoiceId() {
        long invoiceId = generateInvoiceId();
        txtInvoiceID.setText(String.valueOf(invoiceId));
        txtInvoiceID.setDisable(true);
        txtInvoiceID.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    private void disableFiled(){
        txtInvoiceDiscount.setText("0");
        txtInvoiceDiscount.setDisable(true);
        txtInvoiceTotal.setDisable(true);
        txtInvoiceBalance.setEditable(false);
        txtStockID.setEditable(false);
        txtProduct.setEditable(false);
        txtPrice.setEditable(false);
        txtAvbQty.setEditable(false);
        txtInvoiceDiscount.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
        txtInvoiceTotal.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
        txtInvoiceBalance.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
    }

    private void allowOnlyNumbers(TextField field) {

        field.setTextFormatter(new TextFormatter<>(change -> {

            String newText = change.getControlNewText();

            // Allow only numbers with optional decimal point
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }

            return null;
        }));
    }

    private void setCellValueForTable() {
        colStockID.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private long generateInvoiceId() {

        long timeStamp = System.currentTimeMillis();
        int randomPart = (int) (Math.random() * 900) + 100;

        String uniquePart = String.valueOf(timeStamp).substring(4);

        return Long.parseLong(uniquePart + randomPart);
    }

    private void removeRaw(){
        tblInvoice.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                InvoiceTM selected = (InvoiceTM) tblInvoice.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    invoiceList.remove(selected);

                    calculateNetTotal();
                }
            }
        });
    }

    private void clear(){
        txtStockID.clear();
        txtProduct.clear();
        txtPrice.clear();
        txtAvbQty.clear();
        txtQty.clear();
    }

    private void calculateTotalLabel(){
        txtQty.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.isEmpty()) {
                lblTotal.setText("0.00");
                return;
            }

            try {
                double price = Double.parseDouble(txtPrice.getText());
                double qty = Double.parseDouble(newValue);

                if (qty > 0) {
                    double total = price * qty;
                    lblTotal.setText(String.format("%.2f", total));
                } else {
                    lblTotal.setText("0.00");
                }

            } catch (NumberFormatException e) {
                lblTotal.setText("0.00");
            }
        });
    }

    private void calculateNetTotal() {

        double total = 0;

        for (InvoiceTM item : invoiceList) {
            total += item.getTotal();
        }

        txtInvoiceTotal.setText(String.format("%.2f", total));

        calculateFinalAmounts();

    }

    private void calculateFinalAmounts() {

        try {

            double total = 0;

            if (!txtInvoiceTotal.getText().isEmpty()) {
                total = Double.parseDouble(txtInvoiceTotal.getText());
            }

            double discountPercent = 0;

            if (!txtInvoiceDiscount.getText().isEmpty()) {
                discountPercent = Double.parseDouble(txtInvoiceDiscount.getText());
            }

            double discountAmount = total * discountPercent / 100;
            double finalTotal = total - discountAmount;

            double payment = 0;

            if (!txtInvoicePayment.getText().isEmpty()) {
                payment = Double.parseDouble(txtInvoicePayment.getText());
            }

            // 🔥 THIS IS YOUR REQUIRED LOGIC
            double balance = payment - finalTotal;

            txtInvoiceBalance.setText(String.format("%.2f", balance));

            if (balance < 0) {
                txtInvoiceBalance.setStyle("-fx-text-fill: red;");
            } else {
                txtInvoiceBalance.setStyle("-fx-text-fill: green;");
            }

        } catch (NumberFormatException e) {
            txtInvoiceBalance.setText("0.00");
        }
    }

    private boolean isValid() {

        if (txtCutomerMobile.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer mobile is required!").show();
            return false;
        }

        if (txtStockID.getText().trim().isEmpty() ||
                txtPrice.getText().trim().isEmpty() ||
                txtAvbQty.getText().trim().isEmpty() ||
                txtProduct.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a stock!").show();
            return false;
        }

        try {

            if (Double.parseDouble(txtQty.getText()) <= 0) {
                new Alert(Alert.AlertType.ERROR, "Quantity must be greater than zero!").show();
                return false;
            }

            if (Double.parseDouble(txtQty.getText()) > Double.parseDouble(txtAvbQty.getText())) {
                new Alert(Alert.AlertType.ERROR, "Quantity exceeds available stock!").show();
                return false;
            }

            if (Double.parseDouble(txtPrice.getText()) <= 0) {
                new Alert(Alert.AlertType.ERROR, "Invalid price!").show();
                return false;
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values!").show();
            return false;
        }

        return true;
    }


    @FXML
    void btnAddOnAction(ActionEvent event) {

        if (!isValid()) return;

        int stockId = Integer.parseInt(txtStockID.getText());
        String medicine = txtProduct.getText();
        double price = Double.parseDouble(txtPrice.getText());
        double qty = Double.parseDouble(txtQty.getText());
        double availableQty = Double.parseDouble(txtAvbQty.getText());

        boolean alreadyExists = false;

        for (InvoiceTM item : invoiceList) {

            if (item.getStockId() == stockId) {

                double newQty = item.getQty() + qty;

                if (newQty > availableQty) {
                    new Alert(Alert.AlertType.ERROR, "Quantity exceeds available stock!").show();
                    return;
                }

                item.setQty(newQty);
                item.setTotal(newQty * price);

                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {

            invoiceList.add(new InvoiceTM(
                    stockId,
                    medicine,
                    price,
                    qty,
                    qty * price
            ));
        }

        tblInvoice.refresh();
        calculateNetTotal();
        clear();

    }


    @FXML
    void btnStockOnAction(ActionEvent event) {
        FXMLLoader loader = loadWindow("/view/cashierView/stockView.fxml", "Stock Details View");

        if (loader != null) {
            stockViewController controller = loader.getController();

            controller.setListener(stock -> {
                txtStockID.setText(String.valueOf(stock.getId()));
                txtProduct.setText(stock.getMedicine());
                txtPrice.setText(String.valueOf(stock.getPrice()));
                txtAvbQty.setText(String.valueOf(stock.getStockQty()));
            });
        }
    }

    private FXMLLoader loadWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

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

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

        if (invoiceList.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invoice is empty!").show();
            return;
        }

        if (txtCutomerMobile.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer mobile is required!").show();
            return;
        }

        double balance = Double.parseDouble(txtInvoiceBalance.getText());

        if (balance < 0) {
            new Alert(Alert.AlertType.ERROR, "Payment is not enough! Cannot complete invoice.").show();
            return;
        }

        try {

            // Convert TableModel → Entity Items
            List<InvoiceItem> itemList = new ArrayList<>();

            for (InvoiceTM tm : invoiceList) {
                itemList.add(new InvoiceItem(
                        0,
                        tm.getQty(),
                        tm.getPrice(),
                        Long.parseLong(txtInvoiceID.getText()),
                        (int) tm.getStockId()
                ));
            }

            //Create Invoice Entity
            Invoice invoice = new Invoice(
                    Long.parseLong(txtInvoiceID.getText()),
                    LocalDate.now(),
                    txtCutomerMobile.getText(),
                    Double.parseDouble(txtInvoicePayment.getText()),
                    Double.parseDouble(txtInvoiceDiscount.getText()),
                    Double.parseDouble(txtInvoiceBalance.getText()),
                    Double.parseDouble(txtInvoiceTotal.getText()),
                    1,
                    1,
                    itemList
            );

            boolean isPlaced = invoiceService.placeInvoice(invoice);

            if (isPlaced) {

                new Alert(Alert.AlertType.INFORMATION, "Invoice Saved Successfully!").show();

                invoiceList.clear();
                tblInvoice.refresh();
                setInvoiceId();

                txtInvoicePayment.clear();
                txtInvoiceBalance.clear();
                txtInvoiceTotal.clear();
                txtCutomerMobile.clear();

            } else {
                new Alert(Alert.AlertType.ERROR, "Invoice Save Failed!").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Transaction Failed!").show();
            e.printStackTrace();
        }
    }

}
