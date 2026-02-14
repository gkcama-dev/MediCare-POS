package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      setLowStockListDesign();
      loadLowStockSampleData();
      setExpiringDesign();
      loadExpiringSampleData();
    }

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

    private void loadLowStockSampleData() {
        ObservableList<String> sampleData = FXCollections.observableArrayList(
                "Paracetamol 500mg - 10 units left",
                "Amoxicillin 250mg - 5 units left",
                "Panadeine - 8 units left",
                "Vitamin C - 2 units left",
                "Metformin 500mg - 12 units left",
                "Atorvastatin 20mg - 3 units left",
                "Omeprazole 20mg - 0 units left (Out of Stock)",
                "Salbutamol Inhaler - 2 units left",
                "Insulin Pen - 1 unit left"
        );
        if (lstLowStock != null) {
            lstLowStock.setItems(sampleData);
        }
    }

    private void setLowStockListDesign() {
        lstLowStock.setCellFactory(lv -> new ListCell<String>(){

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

                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }else{
                    Image image = new Image(getClass().getResourceAsStream("/icon/warningred.png"));
                    imageView.setImage(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);

                    label.setText(item);
                    setGraphic(hBox);
                }
            }
        });
    }

    private void loadExpiringSampleData() {
        ObservableList<String> sampleData = FXCollections.observableArrayList(
                "Paracetamol 500mg - 10 units left",
                "Amoxicillin 250mg - 5 units left",
                "Panadeine - 8 units left",
                "Vitamin C - 2 units left",
                "Metformin 500mg - 12 units left",
                "Atorvastatin 20mg - 3 units left",
                "Omeprazole 20mg - 0 units left (Out of Stock)",
                "Salbutamol Inhaler - 2 units left",
                "Insulin Pen - 1 unit left"
        );
        if (lstExpiring != null) {
            lstExpiring.setItems(sampleData);
        }
    }

    private void setExpiringDesign() {
        lstExpiring.setCellFactory(lv -> new ListCell<String>(){

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

                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }else{
                    Image image = new Image(getClass().getResourceAsStream("/icon/warningyellow.png"));
                    imageView.setImage(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);

                    label.setText(item);
                    setGraphic(hBox);
                }
            }
        });
    }

}
