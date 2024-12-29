package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.input.KeyEvent;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardingController {

    @FXML
    private Polygon backBtn;
    @FXML
    private ComboBox<String> boardingStatus;
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
    private TextField boardingStatusField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField totalCost;
    @FXML
    private Label capacityField;

    //Boarding statuses
    ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Confirmed", "Cancelled", "Completed");

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(BoardingController.class.getName());

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Boarding currentBoardingPage = appState.getCurrentBoardingPage();
        BoardingRecord selectedBoarding = BoardingRecord.getSelectedBoarding();

        //if ADD
        if(currentBoardingPage == AppState.Boarding.ADD){
            this.startDate.setValue(LocalDate.now());
            this.endDate.setValue(LocalDate.now());
            Date startDate = Date.valueOf(this.startDate.getValue());
            Date endDate = Date.valueOf(this.endDate.getValue());
            boardingStatus.setItems(statusList);
            totalCost.setText(String.valueOf(calculateTotalCost(startDate,endDate)));
            capacityField.setText(String.valueOf(BoardingRecord.getInstance().getRemainingCapacity()));
        }

        //if NOT add
        if(currentBoardingPage != AppState.Boarding.ADD){
            if (selectedBoarding != null) {
                petID.setText(String.valueOf(selectedBoarding.getPetID()));
                totalCost.setText(String.valueOf(selectedBoarding.getTotalCost()));

                //if EDIT
                if (currentBoardingPage == AppState.Boarding.EDIT) {
                    boardingStatus.setItems(statusList);
                    Date startDate = selectedBoarding.getStartDate();
                    Date endDate = selectedBoarding.getEndDate();
                    if (selectedBoarding.getStartDate() != null && selectedBoarding.getEndDate() != null) {
                        LocalDate startDateL = selectedBoarding.getStartDate().toLocalDate();
                        LocalDate endDateL = selectedBoarding.getEndDate().toLocalDate();
                        this.startDate.setValue(startDateL);
                        this.endDate.setValue(endDateL);
                        boardingStatus.setValue(selectedBoarding.getStatus());
                    }
                    selectedBoarding.setTotalCost(calculateTotalCost(startDate,endDate));
                    totalCost.setText(String.valueOf(selectedBoarding.getTotalCost()));
                    capacityField.setText(String.valueOf(BoardingRecord.getInstance().getRemainingCapacity()));
                }

                //if VIEW
                if (currentBoardingPage == AppState.Boarding.VIEW) {
                    String startDate = selectedBoarding.getStartDate() != null ? selectedBoarding.getStartDate().toString() : "";
                    String endDate = selectedBoarding.getEndDate() != null ? selectedBoarding.getEndDate().toString() : "";
                    Date startDateD = selectedBoarding.getStartDate();
                    Date endDateD = selectedBoarding.getEndDate();
                    startDateField.setText(startDate);
                    endDateField.setText(endDate);
                    boardingStatusField.setText(selectedBoarding.getStatus());
                    selectedBoarding.setTotalCost(calculateTotalCost(startDateD,endDateD));
                    totalCost.setText(String.valueOf(selectedBoarding.getTotalCost()));                }
            }
        }
    }

    public void addBoarding () throws IOException {
        try {
            // Validate inputs
            if(validateBoardingInputs()){
                return;
            }

            int petID = Integer.parseInt(this.petID.getText().trim());
            LocalDate startDate = this.startDate.getValue();
            LocalDate endDate = this.endDate.getValue();
            Date startDateD = Date.valueOf(this.startDate.getValue());
            Date endDateD = Date.valueOf(this.endDate.getValue());
            double TotalCost = calculateTotalCost(startDateD,endDateD);
            String status = boardingStatus.getValue();

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into BoardingReservations table
            String query = "INSERT INTO BoardingReservations (StartDate, EndDate, PetID, EmployeeID, Status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            statement.setInt(3, petID);
            statement.setInt(4, User.getEmployeeID());
            statement.setString(5, status);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add boarding? Please double check fields."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Boarding Reservation added successfully.");
                clearBoardingFields();
            } else {
                System.out.println("Failed to add boarding reservation.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding boarding reservation.");
        }
        capacityField.setText(String.valueOf(BoardingRecord.getInstance().getRemainingCapacity()));
    }

    public void saveBoardingChanges () throws IOException {
        try {
            if(validateBoardingInputs()){
                return;
            }

            int petID = Integer.parseInt(this.petID.getText().trim());
            LocalDate startDate = this.startDate.getValue();
            LocalDate endDate = this.endDate.getValue();
            Date startDateD = Date.valueOf(this.startDate.getValue());
            Date endDateD = Date.valueOf(this.endDate.getValue());
            double TotalCost = calculateTotalCost(startDateD,endDateD);
            String status = boardingStatus.getValue();
            int employeeID = User.getEmployeeID();

            RecordsController recordsController = BoardingRecord.getInstance().getRecordsController();
            Integer selectedBoardingID = recordsController.getSelectedBoardingID();
            if (selectedBoardingID == null) {
                Alerts.showErrorDialog("No Boarding Reservation Selected", "Please select a boarding reservation to edit.");
                return;
            }

            String updateQuery = "UPDATE BoardingReservations SET StartDate = ?, EndDate = ?, PetID = ?, EmployeeID = ?, Status = ? WHERE ReservationID = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(updateQuery)) {

                statement.setDate(1, Date.valueOf(startDate));
                statement.setDate(2, Date.valueOf(endDate));
                statement.setInt(3, petID);
                statement.setInt(4, employeeID);
                statement.setString(5, status);
                statement.setInt(6, selectedBoardingID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save boarding changes? Please double check fields."))){
                    return;
                }

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Boarding details updated successfully.");
                    NavigationController.switchToRecordsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the boarding details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving boarding changes.");
        }
    }

    public void printBoarding () throws IOException {
        try {
            BoardingRecord selectedBoarding = BoardingRecord.getSelectedBoarding();
            RecordsController recordsController = BoardingRecord.getInstance().getRecordsController();

            if (selectedBoarding == null) {
                Alerts.showErrorDialog("Error", "No boarding selected.");
                return;
            }

            // Collect information for the printout
            Integer selectedBoardingID = recordsController.getSelectedBoardingID();
            String ownerName = selectedBoarding.getOwnerName();
            int ownerID = selectedBoarding.getOwnerID();
            String petName = selectedBoarding.getPetName();
            String petNotes = selectedBoarding.getPetNotes();
            int petID = selectedBoarding.getPetID();
            String startDate = selectedBoarding.getStartDate().toString();
            String endDate = selectedBoarding.getEndDate().toString();
            String boardingStatus = selectedBoarding.getStatus();
            String employeeName = User.getEmployeeName();
            double totalCost = selectedBoarding.getTotalCost();

            // Printable content
            String reminderMessage =
                    "\n\n-----------------------------------IMPORTANT REMINDER-----------------------------------\n" +
                            "Thank you for trusting PAWFECTCare for your pet's boarding needs!\n\n" +
                            "Please ensure the following for a smooth boarding experience:\n" +
                            "1. Your pet's vaccination is up-to-date.\n" +
                            "2. Inform our staff of any special needs, dietary restrictions, or medications.\n" +
                            "3. Pick up your pet promptly at the end of the boarding period.\n" +
                            "4. In case of emergencies, you can contact us at 09651245784.\n\n" +
                            "We strive to provide your pets with the utmost care and comfort. Should you have\n" +
                            "any questions or feedback, feel free to reach out to our team.\n\n" +
                            "We look forward to serving you and your furry friends again soon!\n" +
                            "-----------------------------------------------------------------------------------------";

            String printContent =
                    "------------------PAWFECTCare: Pet Grooming and Boarding Services-------------------\n" +
                            "Boarding Reservation Details\n\n" +
                            "Reservation ID: " + selectedBoardingID + "\n" +
                            "Owner ID: " + ownerID + "\n" +
                            "Owner Name: " + ownerName + "\n" +
                            "Pet ID: " + petID + "\n" +
                            "Pet Name: " + petName + "\n" +
                            "Pet Notes: " + petNotes + "\n" +
                            "Start Date: " + startDate + "\n" +
                            "End Date: " + endDate + "\n" +
                            "Scheduler: " + employeeName + "\n" +
                            "Status: " + boardingStatus + "\n" +
                            "Total Cost: " + totalCost + "\n\n" +
                            reminderMessage;

            PrinterJob printerJob = PrinterJob.getPrinterJob();

            printerJob.setPrintable(new Printable() {
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex >= 1) { // Only 1 page to print
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

            // Show the print dialog to the user
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while printing appointment details.");
        }
    }

    private boolean validateBoardingInputs() {
        String petID = this.petID.getText().trim();
        LocalDate startDate = this.startDate.getValue();
        LocalDate endDate = this.endDate.getValue();
        Date startDateD = Date.valueOf(this.startDate.getValue());
        Date endDateD = Date.valueOf(this.endDate.getValue());
        double TotalCost = calculateTotalCost(startDateD,endDateD);
        String status = boardingStatus.getValue();

        if(BoardingRecord.getInstance().getRemainingCapacity() < 1) {
            Alerts.showAlert("Validation Error", "Capacity Full.");
            return true;
        }

        if (petID.isEmpty()) {
            Alerts.showAlert("Validation Error", "Registered PetID is required.");
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

        if (startDate.isBefore(LocalDate.now())) {
            Alerts.showAlert("Validation Error", "The start date cannot be in the past.");
            return true;
        }

        if (endDate == null) {
            Alerts.showErrorDialog("Invalid Input", "Start Date and End Date cannot be empty.");
            return true;
        }

        if (endDate.isBefore(startDate)) {
            Alerts.showAlert("Validation Error", "The end date should be on or before the start date.");
            return true;
        }

        if (status == null) {
            Alerts.showAlert("Validation Error", "Please set the appointment's status.");
            return true;
        }
        return false;
    }

    private void clearBoardingFields() {
        petID.setText("");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        totalCost.setText("");
        boardingStatus.setValue("");
    }

    public static double calculateTotalCost(Date startDate, Date endDate) {
        // Calculate the difference in time
        long diffInMillies = endDate.getTime() - startDate.getTime();
        // Calculate difference in days
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies);
        double costPerDay = 1000;

        // Ensure at least one day is charged half the overnight price
        if (diffInDays == 0) {
            return (costPerDay/2);
        }

        return diffInDays * costPerDay;
    }

    @FXML
    private void onDateSelected(ActionEvent event) {
        Date startDate = Date.valueOf(this.startDate.getValue());
        Date endDate = Date.valueOf(this.endDate.getValue());
        if(!dateCheck()){
            return;
        }

        totalCost.setText(String.valueOf(calculateTotalCost(startDate,endDate)));
    }

    private boolean dateCheck() {
        LocalDate startDate = this.startDate.getValue();
        LocalDate endDate = this.endDate.getValue();

        // Validate that the selected date is not in the past
        if (startDate.isBefore(LocalDate.now())) {
            Alerts.showAlert("Validation Error", "The start date cannot be in the past.");
            return false;
        }

        // Validate that the selected date is valid
        if (endDate.isBefore(startDate)) {
            Alerts.showAlert("Validation Error", "The end date should be on or before the start date.");
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

    //transitions & effects
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
    public void searchFunction(KeyEvent event) {
        //add search function here
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
    public void polygonHover () throws IOException {
        backBtn.setFill(Color.web("#48d1dd"));
        backBtn.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backBtn.setFill(Color.web("#A1DBDD"));
        backBtn.setCursor(Cursor.DEFAULT);
    }
}

