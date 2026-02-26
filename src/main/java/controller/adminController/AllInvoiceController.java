package controller.adminController;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.tableModel.InvoiceViewTM;
import service.ServiceFactory;
import service.custom.InvoiceService;
import util.ServiceType;

import javafx.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AllInvoiceController implements Initializable {

    @FXML
    private JFXButton btnPrint;

    @FXML
    private TableColumn<InvoiceViewTM, String> colCustomer;

    @FXML
    private TableColumn<InvoiceViewTM, LocalDate> colDate;

    @FXML
    private TableColumn<InvoiceViewTM, Double> colDiscount;

    @FXML
    private TableColumn<InvoiceViewTM, Long> colInvoiceId;

    @FXML
    private TableColumn<InvoiceViewTM, String> colPaymentType;

    @FXML
    private TableColumn<InvoiceViewTM, Double> colTotal;

    @FXML
    private TableColumn<InvoiceViewTM, Double> colUser;

    @FXML
    private TableView<InvoiceViewTM> tblinvoice;

    @FXML
    private TextField txtSearch;

    private  final InvoiceService invoiceService = ServiceFactory.getInstance().getServiceType(ServiceType.INVOICE);

    private final ObservableList<InvoiceViewTM> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadTable();
        initSearchListener();
    }

    private void setCellValueFactory() {

        colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
    }

    private void loadTable() {

        Task<List<InvoiceViewTM>> task = new Task<>() {
            @Override
            protected List<InvoiceViewTM> call() throws Exception {
                return invoiceService.getAllInvoiceForView();
            }
        };

        task.setOnSucceeded(e -> {
            masterData.clear();
            masterData.addAll(task.getValue());
        });

        task.setOnFailed(e -> {
            new Alert(Alert.AlertType.ERROR,
                    task.getException().getMessage()).show();
        });

        new Thread(task).start();
    }

    private void initSearchListener() {

        FilteredList<InvoiceViewTM> filtered =
                new FilteredList<>(masterData, b -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {

            filtered.setPredicate(inv -> {

                if (newVal == null || newVal.isEmpty())
                    return true;

                String key = newVal.toLowerCase();

                return String.valueOf(inv.getId()).contains(key)
                        || inv.getCustomer().toLowerCase().contains(key)
                        || inv.getPaymentType().toLowerCase().contains(key)
                        || inv.getUser().toLowerCase().contains(key)
                        || String.valueOf(inv.getTotal()).contains(key);
            });
        });

        SortedList<InvoiceViewTM> sorted =
                new SortedList<>(filtered);

        sorted.comparatorProperty().bind(tblinvoice.comparatorProperty());

        tblinvoice.setItems(sorted);
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

}
