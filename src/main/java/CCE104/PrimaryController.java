package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.io.IOException;

public class PrimaryController {

    @FXML
    private Button addAppointmentBtn;

    @FXML
    private Button addBoardingBtn;

    @FXML
    private Button addPetBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button recordsBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Polygon backtoDash;

    public void switchToDashboard () throws IOException {
        Main.switchScene("scenes/dashboardAdmin");
    }

    @FXML
    public void switchToRecords () throws IOException {
        Main.switchScene("scenes/recordsAdmin");
    }

    @FXML
    public void switchToReports () throws IOException {
        Main.switchScene("scenes/reportsAdmin");
    }

    @FXML
    public void logOut () throws IOException {
        Main.switchSceneWithFade("scenes/signIn");
    }

    @FXML
    public void addPet () throws IOException {
        Main.switchSceneWithFade("scenes/addPet");
    }

    @FXML
    public void addAppointment () throws IOException {
        Main.switchSceneWithFade("scenes/addAppointment");
    }

    @FXML
    public void addBoarding () throws IOException {
        Main.switchSceneWithFade("scenes/addBoarding");
    }

    @FXML
    public void polygonHover () throws IOException {
        backtoDash.setFill(Color.web("#48d1dd"));
        backtoDash.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backtoDash.setFill(Color.web("#A1DBDD"));
        backtoDash.setCursor(Cursor.DEFAULT);
    }
}
