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

public class SignInController {

    @FXML
    private void switchToSignUp () throws IOException {
        Main.switchSceneWithFade("scenes/signUp");
    }

    @FXML
    private TextField EmployeeIDEmailField;
    @FXML
    private Hyperlink forgotPasswordLink;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField showPassField;
    @FXML
    private ToggleButton showPasswordBtn;
    @FXML
    private Button signInBtn;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/pawfectCareDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Replace with your MySQL root password

    @FXML
    private void initialize() {
        // Configure sign-in button action
        signInBtn.setOnAction(event -> handleSignIn());

        // Configure toggle button for password visibility
        showPasswordBtn.setOnAction(event -> togglePasswordVisibility());

        // Configure forgot password hyperlink action
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
    }

    private void handleSignIn() {
        String identifier = EmployeeIDEmailField.getText().trim(); // Employee ID or email
        String password = passwordField.getText().trim();

        // Validate input
        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Employee ID/Email and Password are required!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Authenticate user with either email or employee ID
            if (authenticateUser(connection, identifier, password)) {
                showAlert(AlertType.INFORMATION, "Success", "Sign-in successful!");
                Main.switchSceneWithFade("scenes/dashboard");
            } else {
                showAlert(AlertType.ERROR, "Error", "Invalid Employee ID/Email or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to sign in: " + e.getMessage());
        }
    }

    private boolean authenticateUser(Connection connection, String identifier, String password) throws Exception {
        String query = "SELECT * FROM Employees WHERE (Email = ? OR EmployeeID = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, identifier); // Email
            stmt.setString(2, identifier); // Employee ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the stored hashed password from the database
                    String storedHashedPassword = rs.getString("Password");

                    // Verify the entered password with the stored hashed password
                    return BCrypt.checkpw(password, storedHashedPassword);
                }
            }
        }
        return false; // No matching user found or password doesn't match
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

    private void handleForgotPassword() {
        // Implement the forgot password functionality
        // This can redirect the user to a password recovery screen or process
        showAlert(AlertType.INFORMATION, "Forgot Password", "Password recovery process will be implemented here.");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
