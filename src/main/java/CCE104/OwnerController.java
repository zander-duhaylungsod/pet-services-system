package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class  OwnerController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private TextField ownerEmail;
    @FXML
    private TextField ownerFirstName;
    @FXML
    private TextField ownerLastName;
    @FXML
    private TextField ownerPhone;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(OwnerController.class.getName());

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Owner currentOwnerPage = appState.getCurrentOwnerPage();

        OwnerRecord selectedOwner = OwnerRecord.getSelectedOwner();

        //if NOT add
        if (currentOwnerPage != AppState.Owner.ADD) {
            if (selectedOwner != null) {
                ownerFirstName.setText(selectedOwner.getFirstName());
                ownerLastName.setText(selectedOwner.getLastName());
                ownerEmail.setText(selectedOwner.getEmail());
                ownerPhone.setText(selectedOwner.getPhone());
            }
        }
    }

    @FXML
    public void addOwnerToDatabase () throws IOException {
        try {
            // Validate inputs
            if (validateOwnerInputs()) {
                return;
            }
            String firstName = ownerFirstName.getText().trim();
            String lastName = ownerLastName.getText().trim();
            String email = ownerEmail.getText().trim();
            String phone = ownerPhone.getText().trim();

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into Owners table
            String query = "INSERT INTO Owners (FirstName, LastName, Email, Phone) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName.isEmpty() ? null : lastName);
            statement.setString(3, email);
            statement.setString(4, phone);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add owner? Please double check all details."))){
                return;
            }
            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Owner registered successfully.");
                clearOwnerFields();
            } else {
                Alerts.showAlert("Error","Failed to register owner.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while registering owner.");
        }
    }

    @FXML
    public void saveOwnerChanges () throws IOException {
        try {
            //validate inputs
            if (validateOwnerInputs()) {
                return;
            }
            String firstName = ownerFirstName.getText();
            String lastName = ownerLastName.getText();
            String email = ownerEmail.getText();
            String phone = ownerPhone.getText();

            //Get selected ID
            RecordsController recordsController = OwnerRecord.getInstance().getRecordsController();
            Integer selectedOwnerID = recordsController.getSelectedOwnerID();
            if (selectedOwnerID == null) {
                Alerts.showErrorDialog("No Owner Selected", "Please select an owner to edit.");
                return;
            }

            //Update changes in the database
            String updateQuery = "UPDATE Owners SET FirstName = ?, LastName = ?, Email = ?, Phone = ? WHERE OwnerID = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, phone);
                stmt.setInt(5, selectedOwnerID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save owner changes? Please double check fields."))){
                    return;
                }

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Owner details updated successfully.");
                    NavigationController.switchToRecordsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the owner details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving owner changes.");
        }
    }

    //validators & cleaners
    private boolean validateOwnerInputs() {
        String firstName = ownerFirstName.getText().trim();
        String lastName = ownerLastName.getText().trim();
        String email = ownerEmail.getText().trim();
        String phone = ownerPhone.getText().trim();

        if (firstName.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your first name is required.");
            return true;  // Return false to indicate validation failure
        }

        if (lastName.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your last name is required.");
            return true;
        }

        if (email.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your email is required.");
            return true;
        }

        if (!isValidEmail(email)) {
            Alerts.showAlert("Error", "Invalid email format!");
            return true;
        }

        if (phone.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your phone no. is required.");
            return true;
        }

        if (!isValidPhoneNumber(phone)) {
            Alerts.showAlert("Error", "Invalid phone number!");
            return true;
        }

        return false;  // If all validations pass, return true
    }

    private void clearOwnerFields() {
        ownerFirstName.setText("");
        ownerLastName.setText("");
        ownerEmail.setText("");
        ownerPhone.setText("");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^09\\d{2}\\d{3}-?\\d{4}$") && phoneNumber.replaceAll("-", "").length() == 11;
    }

    // transitions & effects
    @FXML
    public void polygonHover () throws IOException {
        backBtn.setFill(Color.web("#48d1dd"));
        backBtn.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backBtn.setFill(Color.web("#A1DBDD"));
        backBtn.setCursor(Cursor.DEFAULT);
    }

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
}

