package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

public class BoardingController {

    @FXML
    private Polygon backBtn;
    @FXML
    private ComboBox<?> boardingStatus;
    @FXML
    private Button dashboardBtn;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button logOutBtn;
    @FXML
    private TextField petID;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField totalCost;
    @FXML
    private Label capacityField;

    public void addBoarding () throws IOException {
        //add function here
    }

    public void saveBoardingChanges () throws IOException {
        //add function here
    }

    public void printBoarding () throws IOException {
        //add function here
    }

    public void backFunction () throws IOException {
        AppState.Page currentPage = AppState.getInstance().getCurrentPage();

        if (currentPage == AppState.Page.DASHBOARD) {
            switchToDashboard();
        } else if (currentPage == AppState.Page.RECORDS) {
            switchToRecords();
        } else if (currentPage == AppState.Page.REPORTS) {
            switchToReports();
        }
    }

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
    public void searchFunction(KeyEvent event) {
        //add search function here
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

