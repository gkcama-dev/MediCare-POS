package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MedicineController {

    @FXML
    private JFXButton btnBrand;

    @FXML
    private JFXButton btnCategory;

    @FXML
    void btnBrandOnAction(ActionEvent event) {
        loadWindow("/view/brand.fxml","Brand Management");
    }

    @FXML
    void btnCategoryOnAction(ActionEvent event) {
         loadWindow("/view/category.fxml","Category Management");
    }

    private void loadWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); //like jDialog
            stage.setTitle(title);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

}
