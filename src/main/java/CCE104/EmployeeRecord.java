package CCE104;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeRecord {
    private int employeeID;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(EmployeeRecord.class.getName());

    // Constructor
    public EmployeeRecord(int employeeID, String firstName, String lastName, String phone, String role) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Email FROM Employees WHERE EmployeeID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setInt(1, selectedEmployee.getEmployeeID());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        email = rs.getString("Email");
                    } else {
                        Alerts.showAlert("Error", "No matching EmployeeID found.");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the Email.");
        }
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // Singleton Instance and Selection Handling
    private static EmployeeRecord instance;
    private static EmployeeRecord selectedEmployee;
    private RecordsController recordsController;

    public static EmployeeRecord getSelectedEmployee() {
        return selectedEmployee;
    }
    public static void setSelectedEmployee(EmployeeRecord employee) {
        selectedEmployee = employee;
    }

    private EmployeeRecord() {}

    public static EmployeeRecord getInstance() {
        if (instance == null) {
            instance = new EmployeeRecord();
        }
        return instance;
    }

    public RecordsController getRecordsController() {
        return recordsController;
    }

    public void setRecordsController(RecordsController recordsController) {
        this.recordsController = recordsController;
    }

    @Override
    public String toString() {
        return "EmployeeRecord {" +
                "employeeID=" + employeeID +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
