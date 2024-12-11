package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.*;

public class ReportsPageController {

    @FXML
    private TableView<PaymentRecord> PaymentTable;
    @FXML
    private TableView<ReportRecord> ReportTable;
    @FXML
    private TableColumn<?, ?> amountColumn;
    @FXML
    private TableColumn<?, ?> reportsCreatedByColumn;
    @FXML
    private TableColumn<?, ?> reportsDateColumn;
    @FXML
    private TableColumn<?, ?> reportsIDColumn;
    @FXML
    private TabPane reportsTabPane;
    @FXML
    private TableColumn<?, ?> reportsTitleColumn;
    @FXML
    private TableColumn<?, ?> reportsTypeColumn;
    @FXML
    private Button dashboardBtn;
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
    private BarChart<String, Number> revenueBarChart;
    @FXML
    private PieChart servicePieChart;

    private ObservableList<PaymentRecord> paymentList = FXCollections.observableArrayList();
    private ObservableList<ReportRecord> reportList = FXCollections.observableArrayList();

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

        // Initialize report table columns
        reportsIDColumn.setCellValueFactory(new PropertyValueFactory<>("reportID"));
        reportsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("reportTitle"));
        reportsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        reportsDateColumn.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        reportsCreatedByColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        //load Data
        loadPayments();
        loadReports();
        loadAnalyticsCharts();

        //bind data to table
        PaymentTable.setItems(paymentList);
        ReportTable.setItems(reportList);

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
        AppState.getInstance().setCurrentReportPage(AppState.Report.ADD);
        Main.switchSceneWithFade("scenes/addReport");
    }

    @FXML
    void editReport() throws IOException{
        ReportRecord selectedReport = ReportTable.getSelectionModel().getSelectedItem();

        if (selectedReport == null) {
            Alerts.showAlert("No Selection", "Please select a report to edit.");
            return;
        }

        ReportRecord.setSelectedReport(selectedReport);
        AppState.getInstance().setCurrentReportPage(AppState.Report.EDIT);
        Main.switchSceneWithFade("scenes/editReport");    }

    @FXML
    void viewReport() throws IOException{
        ReportRecord selectedReport = ReportTable.getSelectionModel().getSelectedItem();

        if (selectedReport == null) {
            Alerts.showAlert("No Selection", "Please select a report to view.");
            return;
        }

        ReportRecord.setSelectedReport(selectedReport);
        AppState.getInstance().setCurrentReportPage(AppState.Report.VIEW);
        Main.switchSceneWithFade("scenes/viewReport");
    }

    @FXML
    void deleteReport() throws IOException{
        ReportRecord selectedReport = ReportTable.getSelectionModel().getSelectedItem();

        if (selectedReport == null) {
            Alerts.showAlert("No Selection", "Please select a report to view.");
            return;
        }

        ReportRecord.setSelectedReport(selectedReport);

        Boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected report?");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Reports WHERE ReportID = ?")) {

                stmt.setInt(1, selectedReport.getReportID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    reportList.remove(selectedReport);
                    ReportTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The report has been successfully deleted.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete the report. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alerts.showErrorDialog("Error", "An error occurred while deleting the report.");
            }
        }
    }

    //ID getters
    public Integer getSelectedPaymentID() {
        PaymentRecord selectedPayment = PaymentTable.getSelectionModel().getSelectedItem();
        return selectedPayment != null ? selectedPayment.getPaymentID() : null;
    }

    //ID getters
    public Integer getSelectedReportID() {
        ReportRecord selectedReport = ReportTable.getSelectionModel().getSelectedItem();
        return selectedReport != null ? selectedReport.getReportID() : null;
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

    public void loadReports() {
        reportList.clear();
        String query = "SELECT r.ReportID, r.ReportTitle, r.ReportDate, r.ReportType, e.EmployeeID, CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName " +
                "FROM Reports r JOIN Employees e ON r.EmployeeID = e.EmployeeID;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int reportID = rs.getInt("ReportID");
                String reportTitle = rs.getString("ReportTitle");
                String reportType = rs.getString("ReportType");
                String reportDate = rs.getString("ReportDate");
                int employeeID = rs.getInt("EmployeeID");
                String employeeName = rs.getString("EmployeeName");

                reportList.add(new ReportRecord(
                        reportID,
                        reportTitle,
                        reportType,
                        reportDate,
                        employeeID,
                        employeeName
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAnalyticsCharts() {
        // Set up the axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue");

        // Create series for appointment, boarding, and total revenues
        XYChart.Series<String, Number> appointmentSeries = new XYChart.Series<>();
        appointmentSeries.setName("Appointment Revenue");

        XYChart.Series<String, Number> boardingSeries = new XYChart.Series<>();
        boardingSeries.setName("Boarding Revenue");

        XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
        totalSeries.setName("Total Revenue");

        // SQL query for fetching detailed revenue data
        String query =
                "SELECT " +
                        "    MONTHNAME(p.PaymentDate) AS month_name, " +
                        "    SUM(CASE WHEN p.AppointmentID IS NOT NULL THEN p.Amount ELSE 0 END) AS appointment_revenue, " +
                        "    SUM(CASE WHEN p.ReservationID IS NOT NULL THEN p.Amount ELSE 0 END) AS boarding_revenue, " +
                        "    SUM(p.Amount) AS total_revenue " +
                        "FROM " +
                        "    Payments p " +
                        "GROUP BY " +
                        "    MONTHNAME(p.PaymentDate), MONTH(p.PaymentDate) " +
                        "ORDER BY " +
                        "    MONTH(p.PaymentDate);";


        try (Connection conn = connect(); // Replace with your connection method
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Populate the BarChart series with data
            while (rs.next()) {
                String monthName = rs.getString("month_name");
                double appointmentRevenue = rs.getDouble("appointment_revenue");
                double boardingRevenue = rs.getDouble("boarding_revenue");
                double totalRevenue = rs.getDouble("total_revenue");

                // Add data points to each series
                appointmentSeries.getData().add(new XYChart.Data<>(monthName, appointmentRevenue));
                boardingSeries.getData().add(new XYChart.Data<>(monthName, boardingRevenue));
                totalSeries.getData().add(new XYChart.Data<>(monthName, totalRevenue));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clear previous data and add the new series to the BarChart
        revenueBarChart.getData().clear();
        revenueBarChart.getData().addAll(appointmentSeries, boardingSeries, totalSeries);

        // Service Usage Pie Chart Data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String query2 = "SELECT ServiceName, COUNT(*) AS usageCount FROM Services JOIN Appointments ON Services.ServiceID = Appointments.ServiceID GROUP BY ServiceName";
        try (Connection conn = connect();
             PreparedStatement stmt2 = conn.prepareStatement(query2)) {
            ResultSet rs = stmt2.executeQuery();

            while (rs.next()) {
                pieChartData.add(new PieChart.Data(rs.getString("ServiceName"), rs.getInt("usageCount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        servicePieChart.setData(pieChartData);
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
