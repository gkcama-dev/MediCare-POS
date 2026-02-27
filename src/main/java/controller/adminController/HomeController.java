package controller.adminController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import model.Dashboard;
import service.ServiceFactory;
import service.custom.DashboardService;
import util.ServiceType;
import util.util.noSelectionModel.NoSelectionModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label lblExpiringSoon;

    @FXML
    private Label lblLowStockItem;

    @FXML
    private Label lblTodaySales;

    @FXML
    private Label lblTotalEarning;

    @FXML
    private ListView<String> lstLowStock;

    @FXML
    private ListView<String> lstExpiring;

    private final DashboardService dashboardService =
            ServiceFactory.getInstance().getServiceType(ServiceType.DASHBOARD);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLowStockListDesign();
        setExpiringDesign();

        lstLowStock.setSelectionModel(new NoSelectionModel<String>());
        lstExpiring.setSelectionModel(new NoSelectionModel<String>());
        refreshDashboard();
    }

    private void refreshDashboard() {
        try {
            // Stats (Counts & Earnings)
            Dashboard stats = dashboardService.getDashboardStats();

            java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");

            lblTodaySales.setText(df.format(stats.getTodaySalesCount()));
            lblTotalEarning.setText(df.format(stats.getTodayEarnings()));
            lblLowStockItem.setText(String.valueOf(stats.getLowStockCount()));
            lblExpiringSoon.setText(String.valueOf(stats.getExpiringCount()));

            // Alerts (Lists)
            loadAlertLists();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load Dashboard data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void loadAlertLists() throws Exception {
        // Low
        List<String> lowStockAlerts = dashboardService.getLowStockAlerts();
        lstLowStock.setItems(FXCollections.observableArrayList(lowStockAlerts));

        // Expiring Soon
        List<String> expiringAlerts = dashboardService.getExpiringSoonAlerts();
        lstExpiring.setItems(FXCollections.observableArrayList(expiringAlerts));
    }

    private void setLowStockListDesign() {
        lstLowStock.setCellFactory(lv -> new ListCell<>() {
            private final HBox hBox = new HBox();
            private final Label label = new Label();
            private final ImageView imageView = new ImageView();
            private final Pane spacer = new Pane();

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(label, spacer, imageView);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/icon/warningred.png")));
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    label.setText(item);
                    setGraphic(hBox);
                }
            }
        });
    }


    private void setExpiringDesign() {
        lstExpiring.setCellFactory(lv -> new ListCell<>() {
            private final HBox hBox = new HBox();
            private final Label label = new Label();
            private final ImageView imageView = new ImageView();
            private final Pane spacer = new Pane();

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(label, spacer, imageView);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/icon/warningyellow.png")));
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    label.setText(item);
                    setGraphic(hBox);
                }
            }
        });
    }


}
