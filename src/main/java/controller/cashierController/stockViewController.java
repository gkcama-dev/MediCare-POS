package controller.cashierController;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Stock;
import model.tableModel.StockTM;
import model.tableModel.StockViewTM;
import service.ServiceFactory;
import service.custom.impl.StockViewServiceImpl;
import util.ServiceType;
import util.listener.StockSelectListener;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class stockViewController implements Initializable {

    @FXML
    private TableColumn<StockViewTM, Integer> colId;

    @FXML
    private TableColumn<StockViewTM, String> colMedicine;

    @FXML
    private TableColumn<StockViewTM, Double> colPrice;

    @FXML
    private TableColumn<StockViewTM, Double> colQty;

    @FXML
    private TableView<StockViewTM> tblStock;

    @FXML
    private TextField txtSearch;

    private final StockViewServiceImpl stockViewService = ServiceFactory.getInstance().getServiceType(ServiceType.STOCKVIEW);

    private ObservableList<StockViewTM> obList = FXCollections.observableArrayList();

    private StockSelectListener listener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadTable();
        initTableClickEvent();
        tblStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("stockQty"));

    }

    public void setListener(StockSelectListener listener) {
        this.listener = listener;
    }

    private void initTableClickEvent() {
        tblStock.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                StockViewTM selected =
                        (StockViewTM) tblStock.getSelectionModel().getSelectedItem();

                if (selected == null) return;

                if (selected.getStockQty() <= 0) {

                    new Alert(Alert.AlertType.WARNING,
                            "This item is out of stock!").show();

                    return;
                }

                if (listener != null) {
                    listener.onStockSelected(selected);

                    Stage stage = (Stage) tblStock.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }


    private void loadTable() {

        try {
            Task<ObservableList<StockViewTM>> loadTask = new Task<>() {
                @Override
                protected ObservableList<StockViewTM> call() throws Exception {

                    List<Stock> stockList = stockViewService.getAllStock();
                    ObservableList<StockViewTM> tempData = FXCollections.observableArrayList();

                    for (Stock stock : stockList) {
                        tempData.add(new StockViewTM(
                                stock.getId(),
                                stock.getProductCode(),
                                stock.getSellingPrice(),
                                stock.getQty()
                        ));
                    }
                    return tempData;
                }
            };


            loadTask.setOnSucceeded(event -> {
                obList.setAll(loadTask.getValue());
                tblStock.setItems(obList);


                tblStock.getSortOrder().clear();
                colId.setSortType(TableColumn.SortType.ASCENDING);
                tblStock.getSortOrder().add(colId);
                tblStock.sort();
            });


            loadTask.setOnFailed(event -> {
                Throwable e = loadTask.getException();
                new Alert(Alert.AlertType.ERROR, "Load Failed: " + e.getMessage()).show();
            });


            new Thread(loadTask).start();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
