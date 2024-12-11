package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EmployeeController {

    @FXML
    private Polygon backBtn;
    @FXML
    private TextField branchID;
    @FXML
    private Button dashboardBtn;
    @FXML
    private TextField employeeFirstName;
    @FXML
    private TextField employeeLastName;
    @FXML
    private TextField employeePassword;
    @FXML
    private Label employeeName;
    @FXML
    private TextField employeePhone;
    @FXML
    private TextField employeeEmail;
    @FXML
    private ComboBox<String> employeeRole;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    ObservableList<String> employeeList = FXCollections.observableArrayList("Admin", "Manager", "Receptionist", "Groomer", "Boarding Attendant","Veterinarian","Pet Trainer","Cleaning Staff");

    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Employee currentEmployeePage = appState.getCurrentEmployeePage();
        EmployeeRecord selectedEmployee = EmployeeRecord.getSelectedEmployee();

        employeeRole.setItems(employeeList);

        if (currentEmployeePage == AppState.Employee.EDIT) {
            if (selectedEmployee != null) {
                employeeFirstName.setText(selectedEmployee.getFirstName());
                employeeLastName.setText(selectedEmployee.getLastName());
                employeeEmail.setText(selectedEmployee.getEmail());
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

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            String query = "INSERT INTO Employees (FirstName, LastName, Phone, Role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phone);
            statement.setString(4, role);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add employee?\n Please double check all details."))){
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
            e.printStackTrace();
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

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            String query = "UPDATE Employees SET FirstName = ?, LastName = ?, Phone = ?, Role = ?, Email = ? WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phone);
            statement.setString(4, role);
            statement.setString(5, email);
            statement.setInt(6, selectedEmployeeID);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save employee changes?\n Please double check fields."))){
                return;
            }
            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Changes saved successfully.");
                Main.switchSceneWithFade("scenes/recordsAdmin");
            } else {
                Alerts.showAlert("Error","Failed to save changes.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
        // Validate inputs
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

    @FXML
    private void switchToSignIn () throws IOException {
        Main.switchSceneWithFade("scenes/signIn");
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

    //effects
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
