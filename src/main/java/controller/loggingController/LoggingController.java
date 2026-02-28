package controller.loggingController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;
import util.Session.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggingController implements Initializable {

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private final UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUsername.setOnAction(event -> txtPassword.requestFocus());
        txtPassword.setOnAction(event -> btnLoginAddOnAction(new ActionEvent()));
    }

    @FXML
    void btnLoginAddOnAction(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username is required!").show();
            return;
        }

        if (password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Password is required").show();
            return;
        }

        try {

            User user = userService.login(username, password);

           // Save logged user
            Session.setUser(user);

            if (user.getUserType().equalsIgnoreCase("Admin")) {

                loadDashboard("/view/adminView/dashboard.fxml");

            } else if (user.getUserType().equalsIgnoreCase("Cashier")) {

                loadDashboard("/view/cashierView/invoiceDashboard.fxml");
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Loggin Error").show();
        }
    }

    private void loadDashboard(String fxmlPath) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            root.setOpacity(0);

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("MediCare POS - Dashboard");
            stage.setScene(scene);
            stage.centerOnScreen();

            javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Unable to load dashboard.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}