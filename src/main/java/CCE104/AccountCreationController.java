package CCE104;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountCreationController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField employeeIDField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField showPassField;
    @FXML
    private Button signUpBtn;
    @FXML
    private ToggleButton showPasswordBtn;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private boolean isPasswordVisible = false;

    @FXML
    private void initialize() {
        // Configure sign-up button action
        signUpBtn.setOnAction(event -> handleSignUp());

        // Configure toggle button action for showing/hiding the password
        showPasswordBtn.setOnAction(event -> togglePasswordVisibility());
    }

    private void handleSignUp() {
        String employeeID = employeeIDField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        try {
            Integer.parseInt(employeeID);
        } catch (NumberFormatException e) {
            Alerts.showAlert("Validation Error", "EmployeeID must be a valid number.");
            return;
        }

        if (employeeID.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(AlertType.ERROR, "Error", "Invalid email format!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (password.length() < 8) {
                Alerts.showErrorDialog("Error", "Password must be at least 8 characters long.");
                return;
            }

            if (!doesEmployeeIDExist(connection, employeeID)) {
                showAlert(AlertType.ERROR, "Error", "Employee ID does not exist!");
                return;
            }

            if (doesEmployeeAccountExist(connection, employeeID)) {
                showAlert(AlertType.ERROR, "Error", "Account already exists! Please sign in instead.");
                return;
            }

            if (doesEmailExist(connection, email)) {
                showAlert(AlertType.ERROR, "Error", "Email already exists!");
                return;
            }

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to create account?\nPlease double check fields."))){
                return;
            }

            // Save the details to the database
            saveAccountDetails(connection, Integer.parseInt(employeeID), email, password);
            Alerts.showSuccessDialog("Success", "Account created successfully!");

            // Clear fields
            emailField.setText("");
            employeeIDField.setText("");
            passwordField.setText("");
            showPassField.setText("");
            switchToSignin();
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showErrorDialog("Error", "Failed to create account, please double check details.");
        }
    }

    private boolean doesEmployeeAccountExist (Connection connection, String employeeID) throws Exception {
        String query = "SELECT Password FROM Employees WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, employeeID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("Password");
                    // Check if employee already has a password set (account created)
                    if (storedPassword != null && !storedPassword.trim().isEmpty()) {
                        showAlert(AlertType.WARNING, "Account Exists",
                                "An account already exists for Employee ID: " + employeeID + ". Please use the Sign In page.");
                        return true;
                    }
                }
                return false; // No account exists yet
            }
        }
    }

    private boolean doesEmployeeIDExist(Connection connection, String employeeID) throws Exception {
        String query = "SELECT EmployeeID FROM Employees WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(employeeID));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean doesEmailExist(Connection connection, String email) throws Exception {
        String query = "SELECT Email FROM Employees WHERE Email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void saveAccountDetails(Connection connection, int employeeID, String email, String password) throws Exception {
        String hashedPassword = hashPassword(password); // Hash the password
        String query = "UPDATE Employees SET Email = ?, Password = ? WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword); // Save the hashed password
            stmt.setInt(3, employeeID);

            stmt.executeUpdate();
        }
    }

    // Password Hashing for security
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void togglePasswordVisibility() {
        boolean isToggled = showPasswordBtn.isSelected();
        if (isToggled) {
            showPassField.setText(passwordField.getText());
            showPassField.setVisible(true);
            passwordField.setVisible(false);
            showPasswordBtn.setOpacity(1.0); // Full opacity when toggled
        } else {
            passwordField.setText(showPassField.getText());
            passwordField.setVisible(true);
            showPassField.setVisible(false);
            showPasswordBtn.setOpacity(0.5); // Reduced opacity when not toggled
        }
    }

    @FXML
    private void switchToSignin() throws IOException {
        Main.switchSceneWithFade("scenes/signIn");
    }
}
