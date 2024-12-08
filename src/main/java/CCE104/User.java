package CCE104;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
    private static int employeeID = 1;  // Storing employeeID statically for easy access
    private static String email;
    private static String role;    // New field for role
    private static String employeeName;  // New field for employee's full name

    // Getters and Setters
    public static int getEmployeeID() {
        fetchEmployeeNameAndRole();
        return employeeID;
    }

    public static void setEmployeeID(int employeeID) {
        User.employeeID = employeeID;
        fetchEmployeeNameAndRole();  // Fetch name and role whenever employeeID is set
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        User.role = role;
    }

    public static String getEmployeeName() {
        fetchEmployeeNameAndRole();
        return employeeName;
    }

    // Method to fetch employee's name and role based on the employeeID
    private static void fetchEmployeeNameAndRole() {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String dbUser = "root";
        String dbPassword = "";  // Your database password

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            // Query to fetch the employee's first and last name, and role based on employeeID
            String query = "SELECT FirstName, LastName, Role FROM Employees WHERE EmployeeID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, employeeID);  // Set employeeID parameter

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Retrieve the employee's details
                        String firstName = rs.getString("FirstName");
                        String lastName = rs.getString("LastName");
                        role = rs.getString("Role");  // Set the role

                        // Combine first and last name
                        employeeName = firstName + " " + lastName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., logging or showing an error message)
        }
    }
}
