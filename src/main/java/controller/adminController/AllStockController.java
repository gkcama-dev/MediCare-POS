package controller.adminController;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.tableModel.StockTM;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import service.ServiceFactory;
import service.custom.GRNService;
import service.custom.StockViewService;
import util.ServiceType;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AllStockController implements Initializable {

    @FXML
    private JFXButton btnGRN;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnStock;

    @FXML
    private TableColumn<StockTM, Double> colBuyPrice;

    @FXML
    private TableColumn<StockTM, LocalDate> colEXP;

    @FXML
    private TableColumn<StockTM, Long> colId;

    @FXML
    private TableColumn<StockTM, LocalDate> colMFD;

    @FXML
    private TableColumn<StockTM, String> colMedicine;

    @FXML
    private TableColumn<StockTM, Double> colQty;

    @FXML
    private TableColumn<StockTM, Double> colSellPrice;

    @FXML
    private TableColumn<StockTM, String> colSupplier;

    @FXML
    private TableView<StockTM> tblStcok;

    @FXML
    private TextField txtSearch;

    private final StockViewService stockViewService = ServiceFactory.getInstance().getServiceType(ServiceType.STOCKVIEW);

    private final ObservableList<StockTM> masterData = FXCollections.observableArrayList();

    private FilteredList<StockTM> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadTable();
        initSearchListener();
        setActiveButton();
    }

    private void setActiveButton() {
        btnStock.setDisable(true);
        btnGRN.setDisable(false);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("grnId"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("product"));
        colMFD.setCellValueFactory(new PropertyValueFactory<>("mfd"));
        colEXP.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadTable() {

        try {
            List<StockTM> stockList = stockViewService.getAllStockForStockView();

            masterData.clear();
            masterData.addAll(stockList);

            filteredData = new FilteredList<>(masterData, p -> true);

            SortedList<StockTM> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblStcok.comparatorProperty());

            tblStcok.setItems(sortedData);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void initSearchListener() {

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(stock -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String keyword = newValue.toLowerCase();

                if (String.valueOf(stock.getGrnId()).toLowerCase().contains(keyword)) {
                    return true;
                } else if (stock.getSupplier().toLowerCase().contains(keyword)) {
                    return true;
                } else if (stock.getProduct().toLowerCase().contains(keyword)) {
                    return true;
                } else if (String.valueOf(stock.getBuyingPrice()).contains(keyword)) {
                    return true;
                } else if (String.valueOf(stock.getSellingPrice()).contains(keyword)) {
                    return true;
                } else if (String.valueOf(stock.getQty()).contains(keyword)) {
                    return true;
                }

                return false;
            });
        });
    }

    @FXML
    void btnGrnOnActionBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/view/adminView/allGRN.fxml"));

            Parent view = loader.load();

            AnchorPane rootPane =
                    (AnchorPane) tblStcok.getScene().lookup("#dashroot");

            rootPane.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPrintOnActionBtn(ActionEvent event) {

        try {

            JasperPrint jasperPrint =
                    stockViewService.generateAllStockReport();

            JasperViewer viewer =
                    new JasperViewer(jasperPrint, false);

            viewer.setTitle("All Stock Report");
            viewer.setVisible(true);

            Alert choice = new Alert(Alert.AlertType.CONFIRMATION);
            choice.setTitle("Export Report");
            choice.setHeaderText("Do you want to save this report as PDF?");
            choice.getButtonTypes().setAll(
                    ButtonType.YES,
                    ButtonType.NO
            );

            Optional<ButtonType> result = choice.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Report");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );

                File file = fileChooser.showSaveDialog(
                        tblStcok.getScene().getWindow()
                );

                if (file != null) {

                    JasperExportManager.exportReportToPdfFile(
                            jasperPrint,
                            file.getAbsolutePath()
                    );

                    viewer.dispose();
                }
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error generating report: " + e.getMessage())
                    .show();
        }

    }

    @FXML
    void btnStockOnActionBtn(ActionEvent event) {

    }

}
