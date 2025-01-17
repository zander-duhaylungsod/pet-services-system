package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.logging.Logger;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;

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
    private Button logOutBtn;
    @FXML
    private TextField petID;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private ComboBox<String> appointmentService;
    @FXML
    private ComboBox<String> appointmentTime;
    @FXML
    private TextField appointmentTimeField;
    @FXML
    private TextField serviceAField;
    @FXML
    private TextField serviceBField;
    @FXML
    private ChoiceBox<?> serviceB;
    @FXML
    private TextField totalCost;

    //Observable Lists
    ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Confirmed", "Cancelled", "Completed");
    ObservableList<String> timeSlots = FXCollections.observableArrayList("09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM" );

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(AppointmentController.class.getName());

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Appointment currentAppointmentPage = appState.getCurrentAppointmentPage();

        AppointmentRecord selectedAppointment = AppointmentRecord.getSelectedAppointment();

        // If ADD or EDIT
        if (currentAppointmentPage == AppState.Appointment.ADD || currentAppointmentPage == AppState.Appointment.EDIT ) {
            appointmentStatus.setItems(statusList);
            appointmentTime.setItems(timeSlots);
            populateServiceComboBox();
        }

        // Check if NOT add
        if (currentAppointmentPage != AppState.Appointment.ADD) {
            if (selectedAppointment != null) {
                java.sql.Date appointmentDateSQL = selectedAppointment.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                petID.setText(String.valueOf(selectedAppointment.getPetID(selectedAppointment.getPetName())));

                String time12hr = convertTo12HourFormat(selectedAppointment.getTime().toString());

                //Check if VIEW
                if(currentAppointmentPage == AppState.Appointment.VIEW) {
                    serviceAField.setText(selectedAppointment.getServiceName());

                    Date sqlDate = selectedAppointment.getDate();
                    if (sqlDate != null) {
                        String appointmentDateString = dateFormat.format(appointmentDateSQL);
                        appointmentDateField.setText(appointmentDateString);
                    }
                    appointmentTimeField.setText(time12hr);

                    appointmentStatusField.setText(selectedAppointment.getStatus());
                }

                //Check if EDIT
                if(currentAppointmentPage != AppState.Appointment.VIEW) {
                    appointmentService.setValue(selectedAppointment.getServiceName());

                    Date sqlDate = selectedAppointment.getDate();
                    if (sqlDate != null) {
                        // Convert java.sql.Date to LocalDate
                        LocalDate localDate = sqlDate.toLocalDate();
                        appointmentDate.setValue(localDate);
                    }
                    appointmentTime.setValue(time12hr);
                    appointmentStatus.setValue(selectedAppointment.getStatus());
                }

                totalCost.setText(String.valueOf(selectedAppointment.getTotalCost()));

            }
        }
    }

    public void addAppointment() throws IOException {
        try {
            // Validate inputs
            if(validateAppointmentInputs()){
                return;
            }

            String petID = this.petID.getText().trim();
            int serviceID = AppointmentRecord.getInstance().getServiceID(appointmentService.getValue());
            LocalDate date = appointmentDate.getValue();
            String time = appointmentTime.getValue();
            String timeFormatted = convertTo24HourFormat(time);
            String status = appointmentStatus.getValue();
            int employeeID = User.getEmployeeID();

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into Appointments table
            String query = "INSERT INTO Appointments (Date, Time, ServiceID, PetID, EmployeeID, Status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(date));
            statement.setString(2, timeFormatted);
            statement.setInt(3, serviceID);
            statement.setInt(4, Integer.parseInt(petID));
            statement.setInt(5, employeeID);
            statement.setString(6, status);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add appointment? Please double check fields."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Appointment added successfully.");
                clearAppointmentFields();
            } else {
                Alerts.showAlert("Error","Failed to add appointment. Please check your inputs.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding appointment.");
        }
    }

    public void saveAppointmentChanges() throws IOException {
        try {
            if(validateAppointmentInputs()){
                return;
            }

            String petID = this.petID.getText().trim();
            int serviceID = AppointmentRecord.getInstance().getServiceID(appointmentService.getValue());
            LocalDate date = appointmentDate.getValue();
            String time = appointmentTime.getValue();
            String timeFormatted = convertTo24HourFormat(time);
            String status = appointmentStatus.getValue();
            int employeeID = User.getEmployeeID();

            RecordsController recordsController = AppointmentRecord.getInstance().getRecordsController();
            Integer selectedAppointmentID = recordsController.getSelectedAppointmentID();
            if (selectedAppointmentID == null) {
                Alerts.showErrorDialog("No Pet Selected", "Please select a pet to edit.");
                return;
            }

            // Update appointment in the database
            String updateQuery = "UPDATE Appointments SET Date = ?, Time = ?, ServiceID = ?, PetID = ?, EmployeeID = ?, Status = ? WHERE AppointmentID = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(updateQuery)) {

                statement.setDate(1, Date.valueOf(date));
                statement.setString(2, timeFormatted);
                statement.setInt(3, serviceID);
                statement.setInt(4, Integer.parseInt(petID));
                statement.setInt(5, employeeID);
                statement.setString(6, status);
                statement.setInt(7, selectedAppointmentID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save appointment changes? Please double check fields."))){
                    return;
                }

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Appointment details updated successfully.");
                    NavigationController.switchToRecordsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the appointment details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving appointment changes.");
        }
    }

    public void printAppointment() throws IOException {
        try {
            AppointmentRecord selectedAppointment = AppointmentRecord.getSelectedAppointment();

            if (selectedAppointment == null) {
                Alerts.showErrorDialog("Error", "No appointment selected.");
                return;
            }

            // Collect information for the printout
            RecordsController recordsController = AppointmentRecord.getInstance().getRecordsController();

            Integer selectedAppointmentID = recordsController.getSelectedAppointmentID();
            int ownerID = selectedAppointment.getOwnerID();
            String ownerName = selectedAppointment.getOwnerName();
            String petName = selectedAppointment.getPetName();
            int petID = selectedAppointment.getPetID(petName);
            String serviceName = selectedAppointment.getServiceName();
            String employeeName = User.getEmployeeName();
            String appointmentDate = selectedAppointment.getDate().toString();
            String appointmentTime = selectedAppointment.getTime().toString();
            String time12hr = convertTo12HourFormat(appointmentTime);
            String appointmentStatus = selectedAppointment.getStatus();
            double totalCost = selectedAppointment.getTotalCost();

            // Printable content
            String reminderMessage =
                    "\n-------------------------------------- Reminder -----------------------------------\n" +
                            "Dear Valued Customer,\n\n" +
                            "Thank you for choosing PAWFECTCare: Pet Grooming and Boarding Services! \nWe value the trust you place in us to care for your beloved pets.\n\n" +
                            "Please keep this appointment confirmation for your reference.\n\n" +
                            "Reminders:\n" +
                            "- Kindly arrive at least 10 minutes before your scheduled appointment to ensure \ntimely service.\n" +
                            "- If you need to reschedule or cancel, please notify us at least 24 hours in advance \nto avoid any inconvenience.\n" +
                            "- Ensure that your pet is in good health and up to date with necessary vaccinations.\n\n" +
                            "For further assistance, please contact us at 09651245784.\n\n" +
                            "We look forward to seeing you and your pet soon!\n\n" +
                            "Warm Regards,\n" +
                            "The PAWFECTCare Team\n" +
                            "-----------------------------------------------------------------------------------------";

            String printContent =
                    "--------------PAWFECTCare: Pet Grooming and Boarding Services--------------\n" +
                    "Appointment Details\n\n" +
                            "Appointment ID: " + selectedAppointmentID + "\n" +
                            "Owner ID: " + ownerID + "\n" +
                            "Owner Name: " + ownerName + "\n" +
                            "Pet ID: " + petID + "\n" +
                            "Pet Name: " + petName + "\n" +
                            "Service Name: " + serviceName + "\n" +
                            "Scheduler: " + employeeName + "\n" +
                            "Appointment Date: " + appointmentDate + "\n" +
                            "Appointment Time: " + time12hr + "\n" +
                            "Status: " + appointmentStatus + "\n" +
                            "Total Cost: " + totalCost + "\n\n" +
                            reminderMessage;

            // PrinterJob instance and print settings
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

                    // Iterate over the lines and print each one
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
            Alerts.showErrorDialog("Error", "An error occurred while printing appointment details.");
        }
    }


    private String convertTo24HourFormat(String time) {
        // Formatter for 12-hour time
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        try {
            LocalTime localTime = LocalTime.parse(time, inputFormatter);
            // Convert to 24-hour format (HH:mm)
            return localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing time: " + time);
            return null;
        }
    }

    private String convertTo12HourFormat(String time) {
        // Formatter for 24-hour time
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            LocalTime localTime = LocalTime.parse(time, inputFormatter);
            // Convert to 12-hour format (hh:mm a)
            return localTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing time: " + time);
            return null;
        }
    }

    private boolean validateAppointmentInputs() {
        String petID = this.petID.getText().trim();
        String service = this.appointmentService.getValue();
        LocalDate date = appointmentDate.getValue();
        String time = appointmentTime.getValue();
        String timeFormatted = convertTo24HourFormat(time);
        String status = appointmentStatus.getValue();

        if (petID.isEmpty()) {
            Alerts.showAlert("Validation Error", "Registered PetID is required.");
            return true;
        }

        if (service == null) {
            Alerts.showAlert("Validation Error", "Please select a service.");
            return true;
        }

        if (date == null) {
            Alerts.showAlert("Validation Error", "Please choose an available date.");
            return true;
        }

        if (date.isBefore(LocalDate.now())) {
            Alerts.showAlert("Validation Error", "The appointment date cannot be in the past.");
            return true;
        }

        if (status == null) {
            Alerts.showAlert("Validation Error", "Please set the appointment's status.");
            return true;
        }

        try {
            Integer.parseInt(petID);
        } catch (NumberFormatException e) {
            Alerts.showAlert("Validation Error", "Pet ID must be a valid number.");
            return true;
        }

        if (!isPetIDRegistered(petID)) {
            Alerts.showAlert("Validation Error", "The provided PetID is not registered in the system.");
            return true;
        }

        if (time == null || time.isEmpty()) {
            Alerts.showAlert("Validation Error", "Please select a valid time for the appointment.");
            return true;
        }

        try {
            assert timeFormatted != null;
            LocalDateTime appointmentDateTime = LocalDateTime.of(date, LocalTime.parse(timeFormatted));
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                Alerts.showAlert("Validation Error", "The appointment time cannot be in the past.");
                return true;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing: " + time + "\n" + e);
        }

        if (isAppointmentTimeOccupied(date, timeFormatted)) {
            Alerts.showAlert("Validation Error", "The selected time slot is already occupied. Please choose a different time.");
            return true;
        }

        return false;
    }

    private boolean isAppointmentTimeOccupied(LocalDate date, String timeFormatted) {
        String query = "SELECT COUNT(*) " +
                "FROM Appointments " +
                "WHERE Date = ? AND Time = ? AND (Status != 'Cancelled' OR Status != 'Completed')";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Convert LocalDate to sql Date
            statement.setDate(1, Date.valueOf(date));
            statement.setString(2, timeFormatted);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                // If editing an appointment, allow the current appointment to overlap
                if (AppState.getInstance().getCurrentAppointmentPage() == AppState.Appointment.EDIT) {
                    return count > 1;
                }
                return count > 0; // Any occupied slot
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred", e);
        }
        return false;
    }

    @FXML
    private void serviceComboBoxOnAction() {
        totalCost.setText(String.valueOf(AppointmentRecord.getInstance().getTotalCost(appointmentService.getValue())));
    }

    private void populateServiceComboBox() {
    ObservableList<String> serviceList = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT serviceName FROM Services";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String serviceName = rs.getString("serviceName");
                    serviceList.add(serviceName);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
        appointmentService.setItems(serviceList);
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
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred", e);
            Alerts.showAlert("Error", "Failed to validate PetID.");
        }

        return false; // If something goes wrong or PetID is not found
    }

    private void clearAppointmentFields(){
        petID.setText("");
        appointmentService.setValue("");
        appointmentDate.setValue(LocalDate.now());
        appointmentTime.setValue("");
        appointmentStatus.setValue("");
        totalCost.setText("");
    }

    @FXML
    private void onDateSelected(ActionEvent event) {
        // When a new date is selected, update the available time slots.
        LocalDate selectedDate = appointmentDate.getValue();

        if (selectedDate != null) {
            updateAvailableTimeSlots(selectedDate);
        }
    }

    private void updateAvailableTimeSlots(LocalDate selectedDate) {
        // Query the database for all appointments on the selected date
        String query = "SELECT Time FROM Appointments WHERE Date = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, Date.valueOf(selectedDate));
            ResultSet resultSet = statement.executeQuery();

            // Create a list of unavailable time slots
            ObservableList<String> unavailableTimeSlots = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String bookedTime = convertTo12HourFormat(resultSet.getString("Time"));
                System.out.println(bookedTime);
                unavailableTimeSlots.add(bookedTime); // Ensure this is in the same format as timeSlots
            }

            // Filter out the unavailable time slots from the available slots
            ObservableList<String> availableTimeSlots = FXCollections.observableArrayList();
            for (String timeSlot : timeSlots) {
                if (!unavailableTimeSlots.contains(timeSlot)) {
                    availableTimeSlots.add(timeSlot);  // Add to available list if it's not booked
                }
            }

            // Update the ComboBox with available time slots
            appointmentTime.setItems(availableTimeSlots);
            if (availableTimeSlots.isEmpty()) {
                Alerts.showAlert("No Available Slots", "There are no available time slots for the selected date.");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred", e);
            Alerts.showAlert("Error", "Failed to load available time slots.");
        }
    }


    //transitions & effects
    @FXML
    public void polygonHover () throws IOException {
        backBtn.setFill(Color.web("#48d1dd"));
        backBtn.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backBtn.setFill(Color.web("#A1DBDD"));
        backBtn.setCursor(Cursor.DEFAULT);
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
}
