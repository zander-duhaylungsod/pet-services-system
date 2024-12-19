package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentController {


    @FXML
    private TextField appointmentID;
    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Label remainingAmount;
    @FXML
    private Button logOutBtn;
    @FXML
    private TextField paymentAmount;
    @FXML
    private TextField paymentMethodField;
    @FXML
    private TextField paymentStatusField; 
    @FXML
    private TextField paymentDateField;
    @FXML
    private TextField paymentDate;
    @FXML
    private TextField paymentEmployeeNameField;
    @FXML
    private ComboBox<String> paymentMethod;
    @FXML
    private ComboBox<String> paymentStatus;
    @FXML
    private TextField petID;
    @FXML
    private TextField paymentIDField;
    @FXML
    private TextArea reasonField;
    @FXML
    private TextField refundAmountField;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField reservationID;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(PaymentController.class.getName());

    //Observable lists
    ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Partial Payment", "Full Payment", "Refunded", "Voided");
    ObservableList<String> methodList = FXCollections.observableArrayList("Cash", "GCash", "Credit Card", "Debit Card");

    public void initialize() throws IOException {
        AppState appState = AppState.getInstance();
        AppState.Payment currentPaymentPage = appState.getCurrentPaymentPage();

        PaymentRecord selectedPayment = PaymentRecord.getSelectedPayment();
        //if ADD
        if(currentPaymentPage == AppState.Payment.PAYMENTA || currentPaymentPage == AppState.Payment.PAYMENTB){
            paymentStatus.setItems(statusList);
            paymentMethod.setItems(methodList);
        }

        //if EDIT
        if(currentPaymentPage == AppState.Payment.EDITA || currentPaymentPage == AppState.Payment.EDITB ){
            paymentStatus.setItems(statusList);
            paymentMethod.setItems(methodList);
            paymentDate.setText(selectedPayment.getPaymentDate());
            petID.setText(String.valueOf(selectedPayment.getPetID()));
            paymentAmount.setText(String.valueOf(selectedPayment.getAmount()));
            remainingAmount.setText(String.valueOf(selectedPayment.getRemainingAmount()));
            paymentMethod.setValue(selectedPayment.getMethod());
            paymentStatus.setValue(selectedPayment.getStatus());
            String service = selectedPayment.getService();

            if ("Boarding".equalsIgnoreCase(service)) {
                reservationID.setText(String.valueOf(selectedPayment.getReservationID()));
            } else {
                appointmentID.setText(String.valueOf(selectedPayment.getAppointmentID()));
            }
        }

        //if PRINT
        if(currentPaymentPage == AppState.Payment.PRINTA || currentPaymentPage == AppState.Payment.PRINTB){
            petID.setText(String.valueOf(selectedPayment.getPetID()));
            paymentAmount.setText(String.valueOf(selectedPayment.getAmount()));
            paymentDateField.setText(String.valueOf(selectedPayment.getPaymentDate()));
            remainingAmount.setText(String.valueOf(selectedPayment.getRemainingAmount()));
            paymentMethodField.setText(selectedPayment.getMethod());
            paymentStatusField.setText(selectedPayment.getStatus());
            paymentEmployeeNameField.setText(selectedPayment.getEmployeeName());
            String service = selectedPayment.getService();

            if ("Boarding".equalsIgnoreCase(service)) {
                System.out.println("ResID: " + selectedPayment.getReservationID());
                reservationID.setText(String.valueOf(selectedPayment.getReservationID()));
            } else {
                appointmentID.setText(String.valueOf(selectedPayment.getAppointmentID()));
            }
        }

        if(currentPaymentPage == AppState.Payment.REFUND){
            paymentIDField.setText(String.valueOf(selectedPayment.getPaymentID()));
            refundAmountField.setText(String.valueOf(selectedPayment.getAmount()));
        }
    }

    public void addPayment() {
        try {
            // Validate inputs
            if(!validatePaymentInputs()){
                return;
            }

            String petID = this.petID.getText().trim();
            String paymentAmount = this.paymentAmount.getText().trim();
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            String paymentMethod = this.paymentMethod.getValue();
            String paymentStatus = this.paymentStatus.getValue();
            String reservationID = null;

            if (AppState.getInstance().getCurrentPaymentPage() == AppState.Payment.PAYMENTB) {
                reservationID = this.reservationID.getText().trim();
            }
            String appointmentID = null;
            if (AppState.getInstance().getCurrentPaymentPage() == AppState.Payment.PAYMENTA) {
                appointmentID = this.appointmentID.getText().trim();
            }

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into Payments table
            String query = "INSERT INTO Payments (PaymentTimeStamp, Amount, Method, Status, ReservationID, AppointmentID, EmployeeID) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, timestamp);
            statement.setString(2, paymentAmount);
            statement.setString(3, paymentMethod);
            statement.setString(4, paymentStatus);

            if (reservationID == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                //if Boarding
                statement.setInt(5, Integer.parseInt(reservationID));
            }

            if (appointmentID == null) {
                statement.setNull(6, Types.INTEGER);
            } else {
                //if Appointment
                statement.setInt(6, Integer.parseInt(appointmentID));
            }
                statement.setInt(7, User.getEmployeeID());
            if(Double.parseDouble(remainingAmount.getText() ) < 0){
                Alerts.showAlert("Overpayment","The payment amount exceeds the total cost, please input the exact required.");
                return;
            }

            if(!(Alerts.showConfirmationDialog("Confirmation","Are you sure to add payment? Please double check all fields."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the statement
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                PaymentRecord.getInstance().setPetID(Integer.parseInt(petID));
                Alerts.showSuccessDialog("Success", "Payment added successfully.");
                clearPaymentFields();
            } else {
                Alerts.showAlert("Error","Failed to add payment. Please check your inputs.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding payment.");
        }
    }

    private boolean validatePaymentInputs() {
        AppState appState = AppState.getInstance();
        AppState.Payment currentPaymentPage = appState.getCurrentPaymentPage();
        
        String petID = this.petID.getText().trim();
        String paymentAmount = this.paymentAmount.getText().trim();
        String paymentMethod = this.paymentMethod.getValue();
        String paymentStatus = this.paymentStatus.getValue();
        String reservationID = "";
        String appointmentID = "";

        if (currentPaymentPage == AppState.Payment.PAYMENTB || currentPaymentPage == AppState.Payment.EDITB) {
            reservationID = this.reservationID.getText().trim();
        }
        if (currentPaymentPage == AppState.Payment.PAYMENTA || currentPaymentPage == AppState.Payment.EDITA) {
            appointmentID = this.appointmentID.getText().trim();
        }

        if (petID.isEmpty()) {
            Alerts.showAlert("Validation Error", "Registered PetID is required.");
            return false;
        }

        try {
            Integer.parseInt(petID);
        } catch (NumberFormatException e) {
            Alerts.showAlert("Validation Error", "Pet ID must be a valid number.");
            return false;
        }

        if (!isPetIDRegistered(petID)) {
            Alerts.showAlert("Validation Error", "The provided PetID is not registered in the system.");
            return false;
        }

        if ((currentPaymentPage == AppState.Payment.PAYMENTA || currentPaymentPage == AppState.Payment.EDITA) &&
                !isPetIDandAppointmentIDMatching(petID, appointmentID)) {
            Alerts.showAlert("Validation Error", "Please check the PetID and AppointmentID.");
            return false;
        }

        if ((currentPaymentPage == AppState.Payment.PAYMENTB || currentPaymentPage == AppState.Payment.EDITB) &&
                !isPetIDandReservationIDMatching(petID, reservationID)) {
            Alerts.showAlert("Validation Error", "Please check the PetID and ReservationID.");
            return false;
        }

        if (paymentAmount.isEmpty()) {
            Alerts.showAlert("Validation Error", "Payment amount is required.");
            return false;
        }

        if (paymentMethod == null) {
            Alerts.showAlert("Validation Error", "Please select a payment method.");
            return false;
        }

        if (paymentStatus == null) {
            Alerts.showAlert("Validation Error", "Please set the payment's status.");
            return false;
        }
        
        if (!appointmentID.isEmpty()) {
            try {
                Integer.parseInt(appointmentID);
            } catch (NumberFormatException e) {
                Alerts.showAlert("Validation Error", "Appointment ID must be a valid number.");
                return false;
            }
        } else if (!reservationID.isEmpty()) {
            try {
                Integer.parseInt(reservationID);
            } catch (NumberFormatException e) {
                Alerts.showAlert("Validation Error", "Reservation ID must be a valid number.");
                return false;
            }
        } else {
            Alerts.showAlert("Validation Error", "Either Appointment ID or Reservation ID must be provided.");
            return false;
        }
        return true;
    }

    private boolean isPetIDRegistered(String petID) {
        String query = "SELECT COUNT(*) FROM Pets WHERE PetID = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, petID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the PetID exists
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "Failed to validate PetID.");
        }
        return false; // If something goes wrong or PetID is not found
    }

    private boolean isPetIDandReservationIDMatching(String petID, String reservationID) {
        String query = "SELECT COUNT(*) FROM BoardingReservations WHERE PetID = ? AND ReservationID = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, petID);
            statement.setString(2, reservationID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the reservation exists
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "Failed to validate PetID and ReservationID.");
        }
        return false; // If something goes wrong or no matching reservation is found
    }

    private boolean isPetIDandAppointmentIDMatching(String petID, String appointmentID) {
        String query = "SELECT COUNT(*) FROM Appointments WHERE PetID = ? AND AppointmentID = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, petID);
            statement.setString(2, appointmentID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the appointment exists
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "Failed to validate PetID and AppointmentID.");
        }

        return false; // If something goes wrong or no matching appointment is found
    }
    
    private void clearPaymentFields() {
        AppState appState = AppState.getInstance();
        AppState.Payment currentPaymentPage = appState.getCurrentPaymentPage();

        petID.setText("");
        paymentAmount.setText("");
        paymentMethod.setValue("");
        paymentStatus.setValue("");
        if (currentPaymentPage == AppState.Payment.PAYMENTA || currentPaymentPage == AppState.Payment.EDITA) {
            appointmentID.setText("");
        } else if (currentPaymentPage == AppState.Payment.PAYMENTB || currentPaymentPage == AppState.Payment.EDITB) {
            reservationID.setText("");
        }
    }


    public static double fetchServicePriceByAppointment(int appointmentID) {
        String queryServiceID = "SELECT ServiceID FROM Appointments WHERE AppointmentID = ?";
        String queryServicePrice = "SELECT Price FROM Services WHERE ServiceID = ?";
        double totalCost = 0.0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            //Get the ServiceID from the AppointmentID
            try (PreparedStatement stmtServiceID = connection.prepareStatement(queryServiceID)) {
                stmtServiceID.setInt(1, appointmentID);
                try (ResultSet rsServiceID = stmtServiceID.executeQuery()) {
                    if (rsServiceID.next()) {
                        int serviceID = rsServiceID.getInt("ServiceID");
                        //Get the Price from the ServiceID
                        try (PreparedStatement stmtServicePrice = connection.prepareStatement(queryServicePrice)) {
                            stmtServicePrice.setInt(1, serviceID);
                            try (ResultSet rsServicePrice = stmtServicePrice.executeQuery()) {
                                if (rsServicePrice.next()) {
                                    totalCost = rsServicePrice.getDouble("Price");
                                } else {
                                    Alerts.showAlert("Error", "Price not found for ServiceID: " + serviceID);
                                    totalCost = 0.0;
                                }
                            }
                        }
                    } else {
                        Alerts.showAlert("Error", "ServiceID not found for AppointmentID: " + appointmentID);
                        totalCost = 0.0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            totalCost = 0.0; // Set default to 0 if an error occurs
        }
        return totalCost;
    }

    public static double calculateTotalCost(int reservationID) {
        String query = "SELECT StartDate, EndDate FROM BoardingReservations WHERE ReservationID = ?";
        double costPerDay = 1000;
        double totalCosts = 0.0;

        System.out.println("Attempting to find reservation with ID: " + reservationID);

        // Existing code continues...
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date startDate = rs.getDate("StartDate");
                    Date endDate = rs.getDate("EndDate");

                    // Calculate the difference in time
                    long diffInMillies = endDate.getTime() - startDate.getTime();
                    // Calculate the difference in days
                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies);

                    // Ensure at least one day is charged half the overnight price
                    if (diffInDays == 0) {
                        totalCosts = costPerDay / 2;
                    } else {
                        totalCosts = diffInDays * costPerDay;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving reservation details.");
        }
        return totalCosts;
    }

    private double calculateRemainingAmount() {
        double paymentAmount = 0;
        paymentAmount = Double.parseDouble(this.paymentAmount.getText().trim());
        double totalCost = 0;

        AppState appState = AppState.getInstance();
        AppState.Payment currentPaymentPage = appState.getCurrentPaymentPage();
        if (currentPaymentPage == AppState.Payment.PAYMENTA || currentPaymentPage == AppState.Payment.EDITA) {
            totalCost = fetchServicePriceByAppointment(Integer.parseInt(this.appointmentID.getText().trim()));
            paymentAmount += fetchTotalPaidByAppointment(Integer.parseInt(appointmentID.getText()));
        } else if (currentPaymentPage == AppState.Payment.PAYMENTB || currentPaymentPage == AppState.Payment.EDITB) {
            int reservationID = Integer.parseInt(this.reservationID.getText().trim());
            System.out.println("Reservation ID: " + reservationID);
            totalCost = calculateTotalCost(reservationID);
            paymentAmount += fetchTotalPaidByReservation(reservationID);
        }

        return totalCost - paymentAmount;
    }

    public static double fetchTotalPaidByAppointment(int appointmentID) {
        double totalPaid = 0.0;

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT SUM(Amount) AS TotalPaid FROM Payments WHERE AppointmentID = ? " +
                    "AND (Status = 'Partial Payment' OR Status = 'Full Payment');";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, appointmentID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalPaid = rs.getDouble("TotalPaid");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
        }

        return totalPaid;
    }

    public static double fetchTotalPaidByReservation(int reservationID) {
        double totalPaid = 0.0;

        try {
            String query = "SELECT SUM(Amount) AS TotalPaid FROM Payments WHERE ReservationID = ?" +
                    "AND (Status = 'Partial Payment' OR Status = 'Full Payment');";
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, reservationID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalPaid = rs.getDouble("TotalPaid");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
        }
        return totalPaid;
    }

    public void onActionIDType() throws IOException{
            double remainingAmount = calculateRemainingAmount();
            this.remainingAmount.setText(String.format("%.2f", remainingAmount));
    }

    public void savePaymentChanges() throws IOException {
        try {
            if (!validatePaymentInputs()) {
                return;
            }

            String paymentAmount = this.paymentAmount.getText().trim();
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            String paymentMethod = this.paymentMethod.getValue();
            String paymentStatus = this.paymentStatus.getValue();

            ReportsPageController reportsPageController = PaymentRecord.getInstance().getReportsPageController();
            Integer selectedPaymentID = reportsPageController.getSelectedPaymentID();
            if (selectedPaymentID == null) {
                Alerts.showErrorDialog("No Payment Selected", "Please select a payment to edit.");
                return;
            }
            
            String updateQuery = "UPDATE Payments SET PaymentTimeStamp = ?, Amount = ?, Method = ?, Status = ?, EmployeeID = ? WHERE PaymentID = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(updateQuery)) {

                statement.setTimestamp(1, timestamp);
                statement.setString(2, paymentAmount);
                statement.setString(3, paymentMethod);
                statement.setString(4, paymentStatus);
                statement.setInt(5, User.getEmployeeID());
                statement.setInt(6, selectedPaymentID);

                if(!(Alerts.showConfirmationDialog("Confirmation","Are you sure to update payment? Please double check all changes."))){
                    return;
                }

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Payment details updated successfully.");
                    clearPaymentFields();
                    NavigationController.switchToReportsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the payment details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving payment changes.");
        }
    }

    public void printPayment() throws IOException {
        try {
            AppState appState = AppState.getInstance();
            AppState.Payment currentPaymentPage = appState.getCurrentPaymentPage();

            PaymentRecord selectedPayment = PaymentRecord.getSelectedPayment();
            if (selectedPayment == null) {
                Alerts.showErrorDialog("Error", "No payment selected.");
                return;
            }

            // Collect information for the printout
            int paymentID = selectedPayment.getPaymentID();
            int ownerID = selectedPayment.getOwnerID();
            String ownerName = selectedPayment.getOwnerName();
            String petName = selectedPayment.getPetName();
            int petID = selectedPayment.getPetID();
            double totalCost = 0.0;
            if (currentPaymentPage == AppState.Payment.PRINTA) {
                int appointmentID = selectedPayment.getAppointmentID();
                totalCost = fetchServicePriceByAppointment(appointmentID);
            } else if (currentPaymentPage == AppState.Payment.PRINTB) {
                int reservationID = selectedPayment.getReservationID();
                totalCost = calculateTotalCost(reservationID);
            }
            String serviceName = selectedPayment.getService();
            String employeeName = User.getEmployeeName();
            String paymentDate = selectedPayment.getPaymentDate();
            String paymentStatus = selectedPayment.getStatus();
            double amountPaid = selectedPayment.getAmount();
            double remainingAmount = totalCost - amountPaid;

            // Create the printable content
            String reminderMessage =
                    "\n-------------------------------------- Reminder -----------------------------------\n" +
                            "Dear Valued Customer,\n\n" +
                            "Thank you for choosing PAWFECTCare: Pet Grooming and Boarding Services!\n We are delighted to have the opportunity to care for your cherished pet.\n\n" +
                            "This is a confirmation of your payment. Please keep this receipt for your records.\n\n" +
                            "If you have any questions or need further assistance, please don’t hesitate to contact us:\n" +
                            "- Phone: 09651245784\n" +
                            "Reminders:\n" +
                            "- Keep this receipt as proof of payment for future reference.\n" +
                            "- If there are any discrepancies or issues with your payment, please notify us within \n7 days of this receipt.\n" +
                            "- For refunds or cancellations, our policies are available at our office, just visit our branch!.\n\n" +
                            "We sincerely appreciate your trust in us and look forward to providing the best care \nfor your pet.\n\n" +
                            "Warm Regards,\n" +
                            "The PAWFECTCare Team\n" +
                            "-----------------------------------------------------------------------------------------";

            String printContent =
                    "--------------PAWFECTCare: Pet Grooming and Boarding Services--------------\n" +
                            "Payment Details\n\n" +
                            "Payment ID: " + paymentID + "\n" +
                            "Payment Date: " + paymentDate + "\n" +
                            "Owner ID: " + ownerID + "\n" +
                            "Owner Name: " + ownerName + "\n" +
                            "Pet ID: " + petID + "\n" +
                            "Pet Name: " + petName + "\n" +
                            "Service Name: " + serviceName + "\n" +
                            "Receptionist: " + employeeName + "\n" +
                            "Processed By: " + selectedPayment.getEmployeeName() + "\n" +
                            "Amount Paid: " + amountPaid + "\n" +
                            "Total Cost: " + totalCost + "\n" +
                            "Remaining Amount To Pay: " + remainingAmount + "\n" +
                            "Payment Status: " + paymentStatus + "\n\n" +
                            reminderMessage;

            PrinterJob printerJob = PrinterJob.getPrinterJob();

            printerJob.setPrintable(new Printable() {
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex >= 1) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.setFont(new Font("Serif", Font.PLAIN, 12));
                    g2d.setColor(java.awt.Color.BLACK);

                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Split the content into lines
                    String[] lines = printContent.split("\n");
                    int yPosition = 100;

                    for (String line : lines) {
                        g2d.drawString(line, 100, yPosition);
                        yPosition += 15;
                    }

                    return Printable.PAGE_EXISTS;
                }
            });

            // Show the print dialog
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while printing payment details.");
        }
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

    public void refundPayment() throws IOException {
        PaymentRecord selectedPayment = PaymentRecord.getSelectedPayment();
        String paymentID = paymentIDField.getText();
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        String paymentStatus = selectedPayment.getStatus();
        String refundAmount = refundAmountField.getText();
        String reason = reasonField.getText();

        // Validate input fields
        if(paymentStatus.equalsIgnoreCase("Refunded")){
            Alerts.showAlert("Error", "This payment has already been refunded.");
            return;
        }

        if (paymentID.isEmpty() || refundAmount.isEmpty() || reason.isEmpty()) {
            Alerts.showAlert("Error", "All fields must be filled in before processing the refund.");
            return;
        }

        if (Double.parseDouble(refundAmount) <= 0) {
            Alerts.showAlert("Error", "You can't refund a non-existing amount.");
            return;
        }

        try {
            String insertQuery = "INSERT INTO Refunds (PaymentID, RefundTimeStamp, RefundAmount, Reason) VALUES (?, ?, ?, ?)";
            String updateQuery = "UPDATE Payments SET Status = ? WHERE PaymentID = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                 PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

                insertStmt.setInt(1, Integer.parseInt(paymentID));
                insertStmt.setTimestamp(2, timestamp);
                insertStmt.setDouble(3, Double.parseDouble(refundAmount));
                insertStmt.setString(4, reason);

                if(!(Alerts.showConfirmationDialog("Confirmation","Are you sure to refund payment?"))){
                    return;
                }
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Refund record successfully added.");

                    updateStmt.setString(1, "Refunded");
                    updateStmt.setInt(2, Integer.parseInt(paymentID));
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        Alerts.showSuccessDialog("Success", "Refund successfully processed, and payment status updated.");
                        NavigationController.switchToReportsWithFade();
                    } else {
                        Alerts.showAlert("Warning", "Refund record added, but payment status was not updated.");
                    }
                } else {
                    Alerts.showAlert("Error", "Refund record could not be added.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while processing the refund: " + e.getMessage());
        } catch (NumberFormatException e) {
            Alerts.showAlert("Error", "Payment ID and Refund Amount must be valid numbers.");
        }
    }

    //transitions and effects
    @FXML
    public void switchToDashboard () throws IOException {
        NavigationController.switchToDashboard();
    }

    @FXML
    public void switchToRecords () throws IOException {
        NavigationController.switchToRecords();
    }

    @FXML
    public void switchToReports () throws IOException {
        NavigationController.switchToReports();
    }

    @FXML
    public void logOut () throws IOException {
        NavigationController.logOut();
    }

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
