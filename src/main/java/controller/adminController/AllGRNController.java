package controller.adminController;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.tableModel.GrnTM;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import service.ServiceFactory;
import service.custom.GRNService;
import service.custom.impl.GRNServiceImpl;
import util.ServiceType;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AllGRNController implements Initializable {

    @FXML
    private JFXButton btnGRN;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnStock;

    @FXML
    private TableColumn<GrnTM, LocalDate> colDate;

    @FXML
    private TableColumn<GrnTM, Long> colId;

    @FXML
    private TableColumn<GrnTM, Double> colPayAmount;

    @FXML
    private TableColumn<GrnTM, Double> colQty;

    @FXML
    private TableColumn<GrnTM, String> colSupplier;

    @FXML
    private TableColumn<GrnTM, Double> colTotal;

    @FXML
    private TableView<GrnTM> tblGRN;

    @FXML
    private TextField txtSearch;

    private final GRNService grnService = ServiceFactory.getInstance().getServiceType(ServiceType.GRN);

    private final ObservableList<GrnTM> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadTable();
        initSearchListener();
        setActiveButton();
    }

    private void setActiveButton() {
        btnGRN.setDisable(true);
        btnStock.setDisable(false);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }



    private void loadTable() {

        Task<List<GrnTM>> loadTask = new Task<>() {
            @Override
            protected List<GrnTM> call() throws Exception {
                return grnService.getAllGRNForView();
            }
        };

        loadTask.setOnSucceeded(event -> {
            masterData.clear();
            masterData.addAll(loadTask.getValue());
        });

        loadTask.setOnFailed(event -> {
            Throwable e = loadTask.getException();
            new Alert(Alert.AlertType.ERROR,
                    "Error Loading GRN Table: " + e.getMessage()
            ).show();
            e.printStackTrace();
        });

        new Thread(loadTask).start();
    }

    private void initSearchListener() {

        FilteredList<GrnTM> filteredData = new FilteredList<>(masterData, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(grn -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String keyword = newValue.toLowerCase();

                return String.valueOf(grn.getId()).toLowerCase().contains(keyword)
                        || grn.getSupplier().toLowerCase().contains(keyword)
                        || String.valueOf(grn.getTotal()).contains(keyword)
                        || String.valueOf(grn.getPaidAmount()).contains(keyword)
                        || String.valueOf(grn.getQty()).contains(keyword);
            });
        });

        SortedList<GrnTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblGRN.comparatorProperty());

        tblGRN.setItems(sortedData);
    }

    @FXML
    void btnGrnOnActionBtn(ActionEvent event) {

    }

    @FXML
    void btnPrintOnActionBtn(ActionEvent event) {
        try {

            JasperPrint jasperPrint =
                    grnService.generateAllGRNReport();

            JasperViewer viewer =
                    new JasperViewer(jasperPrint, false);

            viewer.setTitle("All GRN Report");
            viewer.setVisible(true);

            Alert choice = new Alert(Alert.AlertType.CONFIRMATION);
            choice.setTitle("Export Report");
            choice.setHeaderText("Do you want to save this report as PDF?");
            choice.getButtonTypes().setAll(
                    ButtonType.YES,
                    ButtonType.NO
            );

            Optional<ButtonType> result =
                    choice.showAndWait();

            if (result.isPresent() &&
                    result.get() == ButtonType.YES) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Report");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );

                File file = fileChooser.showSaveDialog(
                        tblGRN.getScene().getWindow()
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/view/adminView/allStock.fxml"));

            Parent view = loader.load();

            AnchorPane rootPane =
                    (AnchorPane) tblGRN.getScene().lookup("#dashroot");

            rootPane.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
