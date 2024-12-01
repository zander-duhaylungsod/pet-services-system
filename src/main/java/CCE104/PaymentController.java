package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;

public class PaymentController {

    @FXML
    private TextField appointmentID;
    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Label employeeName;
    @FXML
    private Button logOutBtn;
    @FXML
    private TextField paymentAmount;
    @FXML
    private DatePicker paymentDate;
    @FXML
    private ComboBox<?> paymentMethod;
    @FXML
    private ComboBox<?> paymentStatus;
    @FXML
    private TextField petID;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField reservationID;
    @FXML
    private TextField searchField;

    public void addPayment() throws IOException {
        //add function here
    }

    public void savePaymentChanges() throws IOException {
        //add function here
    }

    public void printPayment() throws IOException {
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

    @FXML
    public void searchFunction(KeyEvent event) {
        //add search function here
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
