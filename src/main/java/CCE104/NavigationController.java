package CCE104;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class NavigationController {

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button recordsBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    public void switchToDashboard () throws IOException {
        dashboardBtn.setStyle("-fx-background-opacity: 100%");
        recordsBtn.setStyle("-fx-background-opacity: 50%");
        reportsBtn.setStyle("-fx-background-opacity: 50%");
        logOutBtn.setStyle("-fx-background-opacity: 50%");
        Main.switchScene("scenes/dashboardAdmin");
    }

    @FXML
    public void switchToRecords () throws IOException {
        dashboardBtn.setStyle("-fx-background-opacity: 50%");
        recordsBtn.setStyle("-fx-background-opacity: 100%");
        reportsBtn.setStyle("-fx-background-opacity: 50%");
        logOutBtn.setStyle("-fx-background-opacity: 50%");
        Main.switchScene("scenes/recordsAdmin");
    }

    @FXML
    public void switchToReports () throws IOException {
        dashboardBtn.setStyle("-fx-background-opacity: 50%");
        recordsBtn.setStyle("-fx-background-opacity: 50%");
        reportsBtn.setStyle("-fx-background-opacity: 100%");
        logOutBtn.setStyle("-fx-background-opacity: 50%");
        Main.switchScene("scenes/reportsAdmin");
    }

    @FXML
    public void logOut () throws IOException {
        dashboardBtn.setStyle("-fx-background-opacity: 50%");
        recordsBtn.setStyle("-fx-background-opacity: 50%");
        reportsBtn.setStyle("-fx-background-opacity: 50%");
        logOutBtn.setStyle("-fx-background-opacity: 100%");
        Main.switchSceneWithFade("scenes/signIn");
    }
}
