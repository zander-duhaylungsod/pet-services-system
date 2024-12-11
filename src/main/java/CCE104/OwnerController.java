package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;

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
    @FXML
    private TextField searchField;
    @FXML
    private Label EmployeeName;
    ObservableList<String> employeeList = FXCollections.observableArrayList("Admin", "Manager", "Receptionist", "Groomer", "Boarding Attendant","Veterinarian","Pet Trainer","Cleaning Staff");

    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Owner currentOwnerPage = appState.getCurrentOwnerPage();

        OwnerRecord selectedOwner = OwnerRecord.getSelectedOwner();

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
    public void searchFunction(KeyEvent event) {
        //add search function here
    }

    @FXML
    public void addOwnerToDatabase () throws IOException {
        try {
            // Validate inputs
            if (!validateOwnerInputs()) {
                return;  // Exit the method early if validation fails
            }
            String firstName = ownerFirstName.getText().trim();
            String lastName = ownerLastName.getText().trim();
            String email = ownerEmail.getText().trim();
            String phone = ownerPhone.getText().trim();

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            // Insert data into Owners table
            String query = "INSERT INTO Owners (FirstName, LastName, Email, Phone) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName.isEmpty() ? null : lastName);
            statement.setString(3, email);
            statement.setString(4, phone);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add owner?\n Please double check all details."))){
                return;
            }
            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                showSuccessDialog("Success", "Owner registered successfully.");
                clearOwnerFields();
            } else {
                showAlert("Error","Failed to register owner.");
            }
        } catch (IllegalArgumentException e) {
            showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while registering owner.");
        }
    }

    @FXML
    public void saveOwnerChanges () throws IOException {
        try {
            if (!validateOwnerInputs()) {
                return;  // Exit the method early if validation fails
            }
            String firstName = ownerFirstName.getText();
            String lastName = ownerLastName.getText();
            String email = ownerEmail.getText();
            String phone = ownerPhone.getText();

            //Get selected ID
            RecordsController recordsController = OwnerRecord.getInstance().getRecordsController();
            Integer selectedOwnerID = recordsController.getSelectedOwnerID();
            if (selectedOwnerID == null) {
                showErrorDialog("No Owner Selected", "Please select an owner to edit.");
                return;
            }

            //Update changes in the database
            String updateQuery = "UPDATE Owners SET FirstName = ?, LastName = ?, Email = ?, Phone = ? WHERE OwnerID = ?";
            try (Connection conn = DriverManager.getConnection(url,user,password);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, phone);
                stmt.setInt(5, selectedOwnerID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save owner changes?\n Please double check fields."))){
                    return;
                }

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showSuccessDialog("Success", "Owner details updated successfully.");
                    Main.switchSceneWithFade("scenes/recordsAdmin");
                } else {
                    showErrorDialog("Update Failed", "No changes were made to the owner details.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while saving owner changes.");
        }
    }

    //validators & cleaners
    private boolean validateOwnerInputs() {
        String firstName = ownerFirstName.getText().trim();
        String lastName = ownerLastName.getText().trim();
        String email = ownerEmail.getText().trim();
        String phone = ownerPhone.getText().trim();

        if (firstName.isEmpty()) {
            showAlert("Validation Error", "Your first name is required.");
            return false;  // Return false to indicate validation failure
        }

        if (lastName.isEmpty()) {
            showAlert("Validation Error", "Your last name is required.");
            return false;
        }

        if (email.isEmpty()) {
            showAlert("Validation Error", "Your email is required.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format!");
            return false;
        }

        if (phone.isEmpty()) {
            showAlert("Validation Error", "Your phone no. is required.");
            return false;
        }

        if (!isValidPhoneNumber(phone)) {
            showAlert("Error", "Invalid phone number!");
            return false;
        }

        return true;  // If all validations pass, return true
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

    // transitons & effects
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

    //dialogs
    public static void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null);
        alert.setContentText(message); alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

