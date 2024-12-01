package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

public class DashboardController {

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
    private TextField searchField;
    @FXML

    public void initialize() {
        AppState.getInstance().setCurrentPage(AppState.Page.DASHBOARD);
    }

    @FXML
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
    public void searchFunction(KeyEvent event) {
        //add search function here
    }
}

