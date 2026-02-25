package controller.cashierController;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableColumn colId;

    @FXML
    private TableColumn colMedicine;

    @FXML
    private TableColumn colPrice;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableView tblStock;

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
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("stockQty"));

        tblStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
            List<Stock> stockList = stockViewService.getAllStock();

            obList.clear();

            for (Stock stock : stockList) {

                obList.add(new StockViewTM(
                        stock.getId(),
                        stock.getProductCode(),
                        stock.getSellingPrice(),
                        stock.getQty()
                ));
            }

            tblStock.setItems(obList);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
