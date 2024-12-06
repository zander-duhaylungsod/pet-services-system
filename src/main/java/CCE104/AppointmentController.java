package CCE104;

import com.google.protobuf.StringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import org.w3c.dom.Text;
import java.sql.Date;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

public class AppointmentController {

    @FXML
    private DatePicker appointmentDate;

    @FXML
    private TextField appointmentDateField;

    @FXML
    private ComboBox<String> appointmentStatus;

    @FXML
    private TextField appointmentStatusField;

    @FXML
    private Polygon backBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Label employeeName;

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
    private ComboBox<String> appointmentService;

    @FXML
    private TextField serviceAField;

    @FXML
    private TextField serviceBField;

    @FXML
    private ChoiceBox<?> serviceB;

    @FXML
    private Button switchToRecords;

    @FXML
    private TextField totalCost;
    ObservableList<String> serviceList = FXCollections.observableArrayList("Full Grooming Package", "Bathing Only", "Fur Styling", "Teeth Cleaning", "Basic Obedience Training", "General Checkup","Full Package Vaccination");
    ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Confirmed", "Cancelled", "Pending");

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Appointment currentAppointmentPage = appState.getCurrentAppointmentPage();

        AppointmentRecord selectedAppointment = AppointmentRecord.getSelectedAppointment();

        // Check if the current action is not ADD
        if (currentAppointmentPage != AppState.Appointment.ADD) {
            if (selectedAppointment != null) {
                petID.setText(selectedAppointment.getPetName());

                java.sql.Date appointmentDateSQL = selectedAppointment.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-YYYY");

                if(currentAppointmentPage == AppState.Appointment.VIEW) {
                    serviceAField.setText(selectedAppointment.getServiceName());

                    Date sqlDate = selectedAppointment.getDate();
                    if (sqlDate != null) {
                        String appointmentDateString = dateFormat.format(appointmentDateSQL);
                        appointmentDateField.setText(appointmentDateString);
                    }

                    appointmentStatusField.setText(selectedAppointment.getStatus());
                }

                if(currentAppointmentPage != AppState.Appointment.VIEW) {
                    appointmentService.setItems(serviceList);
                    appointmentStatus.setItems(statusList);

                    appointmentService.setValue(selectedAppointment.getServiceName());

                    Date sqlDate = selectedAppointment.getDate();
                    if (sqlDate != null) {
                        // Convert java.sql.Date to LocalDate
                        LocalDate localDate = sqlDate.toLocalDate();
                        appointmentDate.setValue(localDate);
                    }

                    appointmentStatus.setValue(selectedAppointment.getStatus());
                }

                totalCost.setText(String.valueOf(selectedAppointment.getTotalCost()));

            }
        }
    }

    public void addAppointment() throws IOException {
        //add function here
    }

    public void saveAppointmentChanges() throws IOException {
        //add function here
    }

    public void printAppointment() throws IOException {
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

    //alerts
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Return true if the user clicked "OK", false otherwise (including "Cancel" or closing the dialog)
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
