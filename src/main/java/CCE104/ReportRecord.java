package CCE104;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportRecord {
    private int reportID;
    private String reportTitle;
    private String reportType;
    private String reportDate; // Using String for UI compatibility
    private String content;
    private String employeeName;
    private String employeeRole;
    private int createdBy;
    private int employeeID;

    // Constructor
    public ReportRecord(int reportID, String reportTitle, String reportType, String reportDate, int createdBy, String employeeName) {
        this.reportID = reportID;
        this.reportTitle = reportTitle;
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.createdBy = createdBy;
        this.employeeName = employeeName;
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getEmployeeID() {
        fetchEmployeeIDWithReportID(reportID);
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getEmployeeName() {
        fetchEmployeeNameAndRole();
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeRole() {
        fetchEmployeeNameAndRole();
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String generatedDate) {
        this.reportDate = generatedDate;
    }

    public String getContent() {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String dbUser = "root";
        String dbPassword = "";  // Your database password

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "SELECT Content FROM Reports WHERE ReportID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, reportID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        content = rs.getString("Content");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., logging or showing an error message)
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    // Singleton Instance and Selection Handling
    private static ReportRecord instance;
    private static ReportRecord selectedReport;
    private ReportsPageController reportsPageController;

    public static ReportRecord getSelectedReport() {
        return selectedReport;
    }

    public static void setSelectedReport(ReportRecord report) {
        selectedReport = report;
    }

    private ReportRecord() {}

    public static ReportRecord getInstance() {
        if (instance == null) {
            instance = new ReportRecord();
        }
        return instance;
    }

    public ReportsPageController getReportsPageController() {
        return reportsPageController;
    }

    public void setReportsController(ReportsPageController reportsPageController) {
        this.reportsPageController = reportsPageController;
    }

    public void fetchEmployeeNameAndRole() {
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
                        employeeRole = rs.getString("Role");  // Set the role

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

    public void fetchEmployeeIDWithReportID(int reportID) {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String dbUser = "root";
        String dbPassword = "";  // Your database password

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            // Query to fetch the employee's first and last name, and role based on employeeID
            String query = "SELECT EmployeeID FROM Reports WHERE ReportID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, reportID);  // Set employeeID parameter

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        employeeID = rs.getInt("EmployeeID");  // Set the role
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., logging or showing an error message)
        }
    }


    @Override
    public String toString() {
        return "ReportRecord{" +
                "reportID=" + reportID +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportType='" + reportType + '\'' +
                ", generatedDate='" + reportDate + '\'' +
                ", content='" + content + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}
