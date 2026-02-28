import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Starter extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/splash.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("MediCare POS - Dashboard");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        stage.setResizable(false);
        stage.show();
    }
}
