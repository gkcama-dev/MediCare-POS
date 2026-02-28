package controller.loggingController;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {


        @FXML
        private ImageView imgLogo;

        @FXML
        private Label lblVersion;

        @FXML
        private AnchorPane mainContainer;

        @FXML
        private ProgressBar splashBar;

        @FXML
        private Label txtLoading;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblVersion.setText("MediCare POS v1.0.0");

        applyFadeIn(mainContainer, 1500);

        startLoading();
    }

    private void applyFadeIn(Node node, int duration) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void startLoading() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String[] messages = {
                        "Connecting to database...",
                        "Loading Products...",
                        "Checking Stock levels...",
                        "Preparing Dashboard...",
                        "Finalizing Components...",
                        "Ready to Start!"
                };

                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);

                    // Progress එක අනුව Message එක මාරු කිරීම
                    if (i < 20) updateMessage(messages[0]);
                    else if (i < 40) updateMessage(messages[1]);
                    else if (i < 60) updateMessage(messages[2]);
                    else if (i < 80) updateMessage(messages[3]);
                    else updateMessage(messages[4]);

                    Thread.sleep(40);
                }
                return null;
            }
        };

        splashBar.progressProperty().bind(task.progressProperty());
        txtLoading.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(event -> {
            // Smooth Fade Out එකකින් පසු Login එකට යාම
            FadeTransition fadeOut = new FadeTransition(Duration.millis(800), mainContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> goToLogin());
            fadeOut.play();
        });

        new Thread(task).start();
    }

    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/login.fxml"));
            Stage stage = (Stage) splashBar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
