package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsPageController {

    @FXML
    private TableView<PaymentRecord> PaymentTable;
    @FXML
    private TableView<?> reportTable;
    @FXML
    private TableColumn<?, ?> amountColumn;
    @FXML
    private TableColumn<?, ?> createdByColumn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private TableColumn<?, ?> dateColumn;
    @FXML
    private Label employeeName;
    @FXML
    private Label employeeRole;
    @FXML
    private Button logOutBtn;
    @FXML
    private TableColumn<?, ?> ownerColumn;
    @FXML
    private TableColumn<?, ?> paymentDateColumn;
    @FXML
    private TableColumn<?, ?> paymentIDColumn;
    @FXML
    private TableColumn<?, ?> paymentStatus;
    @FXML
    private TableColumn<?, ?> petColumn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<?, ?> serviceProvidedColumn;
    @FXML
    private TableColumn<?, ?> titleColumn;
    @FXML
    private TableColumn<?, ?> typeColumn;
    @FXML
    private TabPane reportsTabPane;
    private ObservableList<PaymentRecord> paymentList = FXCollections.observableArrayList();

    public void initialize() {
        PaymentRecord.getInstance().setReportsPageController(this);
        AppState.getInstance().setCurrentPage(AppState.Page.REPORTS);

        PaymentRecord selectedPayment  = PaymentRecord.getSelectedPayment();

        // Initialize payment table columns
        paymentIDColumn.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        petColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        serviceProvidedColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //load Data
        loadPayments();

        //bind data to table
        PaymentTable.setItems(paymentList);

        // Tabpane Setup
        reportsTabPane.getSelectionModel().select(AppState.getInstance().getCurrentTabIndex());
        reportsTabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            AppState.getInstance().setCurrentTabIndex(newValue.intValue());
        });
    }

    @FXML
    public void addPayment() throws IOException {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Appointment", "Appointment", "Boarding Reservation");
        dialog.setTitle("Select Payment Type");
        dialog.setHeaderText("Choose the payment type");
        dialog.setContentText("What are you paying for?");

        dialog.showAndWait().ifPresent(choice -> {
            if (choice.equals("Appointment")) {
                try {
                    AppState.getInstance().setCurrentPaymentPage(AppState.Payment.PAYMENTA);
                    Main.switchSceneWithFade("scenes/addPaymentA");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    AppState.getInstance().setCurrentPaymentPage(AppState.Payment.PAYMENTB);
                    Main.switchSceneWithFade("scenes/addPaymentB");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void editPayment() throws IOException {
        PaymentRecord selectedPayment = PaymentTable.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            Alerts.showAlert("No Selection", "Please select a payment to edit.");
            return;
        }

        // Check service and retrieve the respective ID
        int appointmentID = -1;
        int reservationID = -1;
        String service = selectedPayment.getService();

        // Debug print to verify service type
        System.out.println("Service: " + service);

        if ("Boarding".equalsIgnoreCase(service)) {
            reservationID = selectedPayment.getReservationID();
        } else {
            appointmentID = selectedPayment.getAppointmentID();
        }

        // Debugging outputs
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Reservation ID: " + reservationID);

        if (appointmentID > 0 && reservationID == -1) { // Placeholder check for an Appointment
            PaymentRecord.setSelectedPayment(selectedPayment);
            AppState.getInstance().setCurrentPaymentPage(AppState.Payment.EDITA);
            Main.switchSceneWithFade("scenes/editPaymentA");
        } else if (appointmentID == -1 && reservationID > 0) { // Placeholder check for Boarding Reservation
            PaymentRecord.setSelectedPayment(selectedPayment);
            AppState.getInstance().setCurrentPaymentPage(AppState.Payment.EDITB);
            Main.switchSceneWithFade("scenes/editPaymentB");
        } else {
            Alerts.showAlert("Error", "Unable to determine the type for the selected payment.");
        }
    }

    @FXML
    void printPayment() throws IOException {
        PaymentRecord selectedPayment = PaymentTable.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            Alerts.showAlert("No Selection", "Please select a payment to print.");
            return;
        }

        PaymentRecord.setSelectedPayment(selectedPayment);
        String status = selectedPayment.getStatus();
//        if (!"Full Payment".equals(status) && !"Partial Payment".equals(status)) {
//            Alerts.showAlert("Invalid Status", "Cannot print the payment with the current status.");
//            return;
//        }

        // Check service and retrieve the respective ID
        int appointmentID = -1;
        int reservationID = -1;
        String service = selectedPayment.getService();

        // Debug print to verify service type
        System.out.println("Service: " + service);

        if ("Boarding".equalsIgnoreCase(service)) {
            reservationID = selectedPayment.getReservationID();
        } else {
            appointmentID = selectedPayment.getAppointmentID();
        }

        // Debugging outputs
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Reservation ID: " + reservationID);

        // Determine which scene to navigate to based on the ID values
        if (appointmentID > 0 && reservationID == -1) {
            AppState.getInstance().setCurrentPaymentPage(AppState.Payment.PRINTA);
            System.out.println("Printing Payment A");
            PaymentRecord.setSelectedPayment(selectedPayment);
            Main.switchSceneWithFade("scenes/printPaymentA");
        } else if (appointmentID == -1 && reservationID > 0) {
            AppState.getInstance().setCurrentPaymentPage(AppState.Payment.PRINTB);
            System.out.println("Printing Payment B");
            PaymentRecord.setSelectedPayment(selectedPayment);
            Main.switchSceneWithFade("scenes/printPaymentB");
        } else {
            Alerts.showAlert("Error", "Unable to determine the type for the selected payment.");
        }
    }

    @FXML
    void refundPayment() throws IOException{
        PaymentRecord selectedPayment = PaymentTable.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            Alerts.showAlert("No Selection", "Please select a payment to refund.");
            return;
        }

        PaymentRecord.setSelectedPayment(selectedPayment);
        AppState.getInstance().setCurrentPaymentPage(AppState.Payment.REFUND);
        Main.switchSceneWithFade("scenes/refundPayment");
    }

    @FXML
    void addReport() throws IOException{
        Main.switchSceneWithFade("scenes/addReport");
    }

    @FXML
    void editReport() throws IOException{
        Main.switchSceneWithFade("scenes/editReport");
    }

    @FXML
    void viewReport() throws IOException{
        Main.switchSceneWithFade("scenes/viewReport");
    }

    @FXML
    void deleteReport() throws IOException{
        //add function here
    }

    //ID getters
    public Integer getSelectedPaymentID() {
        PaymentRecord selectedPayment = PaymentTable.getSelectionModel().getSelectedItem();
        return selectedPayment != null ? selectedPayment.getPaymentID() : null;
    }

    //Table Loading --
    public void loadPayments() {
        paymentList.clear();
        String query = "SELECT p.PaymentID, COALESCE(pets.Name, 'Unknown') AS PetName, " +
                "COALESCE(CONCAT(o.FirstName, ' ', o.LastName), 'Unknown Owner') AS OwnerName, " +
                "COALESCE(s.ServiceName, 'Boarding') AS ServiceName, a.AppointmentID, b.ReservationID, " +
                "p.PaymentDate, p.Amount, p.Status " +
                "FROM Payments p " +
                "LEFT JOIN Appointments a ON p.AppointmentID = a.AppointmentID " +
                "LEFT JOIN BoardingReservations b ON p.ReservationID = b.ReservationID " +
                "LEFT JOIN Pets pets ON COALESCE(a.PetID, b.PetID) = pets.PetID " +
                "LEFT JOIN Owners o ON pets.OwnerID = o.OwnerID " +
                "LEFT JOIN Services s ON a.ServiceID = s.ServiceID;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ownerName = rs.getString("OwnerName");
                String petName = rs.getString("PetName");
                String serviceName = rs.getString("ServiceName");
                Integer appointmentID = rs.getObject("AppointmentID") != null ? rs.getInt("AppointmentID") : null;

                if (appointmentID == null || serviceName == null || serviceName.isEmpty()) {
                    serviceName = "Boarding";
                }

                paymentList.add(new PaymentRecord(
                        rs.getInt("PaymentID"),
                        petName,
                        ownerName,
                        serviceName,
                        rs.getDate("PaymentDate"),
                        rs.getDouble("Amount"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //table dependencies & database functions
    private Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = ""; // Replace with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }

    //transitions & effects
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

}
