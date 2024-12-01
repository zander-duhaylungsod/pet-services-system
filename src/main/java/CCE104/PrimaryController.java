package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Polygon backBtn;

    private ObservableList<PetRecord> petRecordsList = FXCollections.observableArrayList();

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
    public void addPetToDatabase () throws IOException {
        //add function here
    }

    @FXML
    public void addOwner () throws IOException {
        Main.switchSceneWithFade("scenes/addOwner");
    }

    @FXML
    public void selectFile () throws IOException {
        //add function here
    }

    //effects
    @FXML
    public void polygonHover () throws IOException {
        backBtn.setFill(Color.web("#48d1dd"));
        backBtn.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backBtn.setFill(Color.web("#A1DBDD"));
        backBtn.setCursor(Cursor.DEFAULT);
    }
}

