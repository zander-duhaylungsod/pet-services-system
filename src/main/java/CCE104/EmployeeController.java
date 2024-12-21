package CCE104;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private TextField employeeFirstName;
    @FXML
    private TextField employeeLastName;
    @FXML
    private TextField employeePhone;
    @FXML
    private TextField employeeEmail;
    @FXML
    private ComboBox<String> employeeRole;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField emailEmployeeID;
    @FXML
    private TextField adminIDEmailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;

    //employee roles
    ObservableList<String> employeeList = FXCollections.observableArrayList("Administrator", "Manager", "Receptionist", "Groomer", "Boarding Attendant","Veterinarian","Pet Trainer","Cleaning Staff");

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(EmployeeController.class.getName());

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Employee currentEmployeePage = appState.getCurrentEmployeePage();
        EmployeeRecord selectedEmployee = EmployeeRecord.getSelectedEmployee();

        //if EDIT or ADD
        if (currentEmployeePage == AppState.Employee.EDIT || currentEmployeePage == AppState.Employee.ADD) {
            employeeRole.setItems(employeeList);
        }

        //if EDIT
        if (currentEmployeePage == AppState.Employee.EDIT) {
            if (selectedEmployee != null) {
                employeeFirstName.setText(selectedEmployee.getFirstName());
                employeeLastName.setText(selectedEmployee.getLastName());
                if(selectedEmployee.getEmail() == null){
                    employeeEmail.setEditable(false);
                    employeeEmail.setText(selectedEmployee.getEmail());
                } else {
                    employeeEmail.setText(selectedEmployee.getEmail());
                    employeeEmail.setEditable(true);
                }
                employeePhone.setText(selectedEmployee.getPhone());
                employeeRole.setValue(selectedEmployee.getRole());
            }
        }
    }

    public void addEmployee() throws IOException {
        try {
            // Validate inputs
            if(!validateEmployeeInputs()){
                return;
            }

            String firstName = employeeFirstName.getText().trim();
            String lastName = employeeLastName.getText().trim();
            String phone = employeePhone.getText().trim();
            String role = employeeRole.getValue();

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "INSERT INTO Employees (FirstName, LastName, Phone, Role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phone);
            statement.setString(4, role);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add employee? Please double check all details."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Employee added successfully.");
                clearEmployeeFields();
            } else {
                Alerts.showAlert("Error","Failed to add employee.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding employee.");
        }
    }

    public void saveEmployeeChanges() throws IOException {
        try {
            // Validate inputs
            if(!validateEmployeeInputsE()){
                return;
            }


            String firstName = employeeFirstName.getText().trim();
            String lastName = employeeLastName.getText().trim();
            String email = employeeEmail.getText();
            String phone = employeePhone.getText().trim();
            String role = employeeRole.getValue();

            //Get selected ID
            RecordsController recordsController = EmployeeRecord.getInstance().getRecordsController();
            Integer selectedEmployeeID = recordsController.getSelectedEmployeeID();
            if (selectedEmployeeID == null) {
                Alerts.showErrorDialog("No Service Selected", "Please select an employee record to edit.");
                return;
            }

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "UPDATE Employees SET FirstName = ?, LastName = ?, Phone = ?, Role = ?, Email = ? WHERE EmployeeID = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phone);
            statement.setString(4, role);
            statement.setString(5, email);
            statement.setInt(6, selectedEmployeeID);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save employee changes? Please double check fields."))){
                return;
            }
            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Changes saved successfully.");
                NavigationController.switchToRecordsWithFade();
            } else {
                Alerts.showAlert("Error","Failed to save changes.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving changes.");
        }
    }

    //validators & cleaners
    private boolean validateEmployeeInputs() {
        String firstName = employeeFirstName.getText().trim();
        String lastName = employeeLastName.getText().trim();
        String phone = employeePhone.getText().trim();
        String role = employeeRole.getValue();

        if (firstName.isEmpty()) {
            Alerts.showAlert("Validation Error", "First Name is required.");
            return false;
        }

        if (lastName.isEmpty()) {
            Alerts.showAlert("Validation Error", "Last Name is required.");
            return false;
        }

        if (phone.isEmpty()) {
            Alerts.showAlert("Validation Error", "Phone No. is required.");
            return false;
        }

        if (!isValidPhoneNumber(phone)) {
            Alerts.showAlert("Validation Error", "Invalid Phone Number.");
            return false;
        }

        if (role == null) {
            Alerts.showAlert("Validation Error", "Please specify the employee's role.");
            return false;
        }

        return true;
    }

    private boolean validateEmployeeInputsE() {
        if(!validateEmployeeInputs()){
            return false;
        }

        String email = employeeEmail.getText();

        if (!(email == null|| email.isEmpty() || email.trim().isEmpty())) {
            if (!isValidEmail(email)) {
                Alerts.showAlert("Validation Error", "Invalid Email.");
                return false;
            }
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^09\\d{2}\\d{3}-?\\d{4}$") && phoneNumber.replaceAll("-", "").length() == 11;
    }

    private void clearEmployeeFields () {
        employeeFirstName.setText("");
        employeeLastName.setText("");
        employeePhone.setText("");
        employeeRole.setValue("");

        if(AppState.getInstance().getCurrentEmployeePage() == AppState.Employee.EDIT){
            employeeEmail.setText("");
        }
    }

    @FXML
    public void verifyAdmin(ActionEvent event) {
        String identifier = adminIDEmailField.getText().trim(); // Employee ID or email
        String password = passwordField.getText().trim();

        // Validate input
        if (identifier.isEmpty() || password.isEmpty()) {
            Alerts.showAlert("Error", "Employee ID/Email and Password are required!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Authenticate user with either email or employee ID
            if (SignInController.authenticateUser(connection, identifier, password)) {
                String role = SignInController.getEmployeeRoleFromDatabase(connection, identifier);
                assert role != null;
                if(role.equalsIgnoreCase("Administrator") || role.equalsIgnoreCase("Manager")) {
                    // Close the current popup first
                    PopUpSwitcher.closeCurrentPopup(true,0.2);

                    Alerts.showSuccessDialog("Success", "Verification Successful!");

                    // Use Platform.runLater to ensure popup is closed before scene switch
                    Platform.runLater(() -> {
                        try {
                            Main.switchSceneWithFade("scenes/resetEmployeePassword");
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                            Alerts.showAlert("Error", "Failed to switch scene: " + e.getMessage());
                        }
                    });
                } else {
                    Alerts.showAlert("Error", "The verifier should be either an Administrator or Employee!");
                }
            } else {
                Alerts.showAlert("Error", "Invalid Employee ID/Email or password!");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error", "Failed to sign in: " + e.getMessage());
        }
    }

    @FXML
    public void resetPassword() throws IOException {
        String iDorEmail = emailEmployeeID.getText();
        String newPassword = this.newPassword.getText();
        String confirmPassword = this.confirmPassword.getText();

        // Input validation
        if (iDorEmail.isEmpty() || newPassword.isEmpty()) {
            Alerts.showErrorDialog("Error", "All fields must be filled.");
            return;
        }

        // Validate new password complexity
        if (newPassword.length() < 8) {
            Alerts.showErrorDialog("Error", "New password must be at least 8 characters long.");
            return;
        }

        // Validate new password complexity
        if (!newPassword.equals(confirmPassword)) {
            Alerts.showErrorDialog("Error", "Passwords does not match.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Prepare statement to check existing credentials
            String checkQuery = "SELECT EmployeeID, Password FROM Employees WHERE EmployeeID = ? OR Email = ?";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, iDorEmail);
                checkStmt.setString(2, iDorEmail);

                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    Alerts.showErrorDialog("Error", "Employee not found.");
                    return;
                }

                int employeeID = rs.getInt("EmployeeID");

                // Hash new password
                String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                // Update password in database
                String updateQuery = "UPDATE Employees SET Password = ? WHERE EmployeeID = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newHashedPassword);
                    updateStmt.setInt(2, employeeID);

                    int rowsAffected = updateStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        Alerts.showSuccessDialog("Success", "Password successfully updated. Please sign in.");
                        AppState.getInstance().setCurrentEmployeePage(AppState.Employee.ADD);
                        Main.switchSceneWithFade("scenes/signIn");
                    } else {
                        Alerts.showErrorDialog("Error", "Failed to update password.");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showErrorDialog("Database Error", "An error occurred while resetting password.");
        }
    }

    //transitions and effects
    public void backFunction () throws IOException {
        AppState.Page currentPage = AppState.getInstance().getCurrentPage();

        if (AppState.getInstance().getCurrentEmployeePage() == AppState.Employee.RESET) {
            switchToSignIn();
        } else if (currentPage == AppState.Page.DASHBOARD) {
            switchToDashboard();
        } else if (currentPage == AppState.Page.RECORDS) {
            switchToRecords();
        } else if (currentPage == AppState.Page.REPORTS) {
            switchToReports();
        }
    }

    @FXML
    private void switchToSignIn () throws IOException {
        Main.switchSceneWithFade("scenes/signIn");
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
