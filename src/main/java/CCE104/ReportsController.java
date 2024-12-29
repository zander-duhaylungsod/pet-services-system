package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReportsController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private TextField employeeIDField;
    @FXML
    private Label employeeName;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button recordsBtn;
    @FXML
    private HTMLEditor reportContentEditor;
    @FXML
    private WebView reportContentView;
    @FXML
    private TextField reportTitleField;
    @FXML
    private ComboBox<String> reportType;
    @FXML
    private Button reportsBtn;
    @FXML
    private Label remainingWordsLabel;

    ObservableList<String> reportTypeList = FXCollections.observableArrayList(
            "Monthly Report",
            "Weekly Report",
            "Daily Report",
            "Performance Review",
            "Financial Summary",
            "Inventory Report",
            "Service Update",
            "Customer Feedback",
            "Event Planning",
            "Reservation Summary",
            "Grooming Summary",
            "Boarding Summary",
            "Yearly Report",
            "Special Report",
            "Sales Report",
            "Employee Review",
            "Health and Wellness",
            "Operational Summary",
            "Audit Report",
            "Incident Report"
    );

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(ReportsController.class.getName());

    // Use WebView to render the HTML
    static WebView webView = new WebView();
    static WebEngine webEngineForPrint = webView.getEngine();

    public void initialize() throws IOException {
        AppState appState = AppState.getInstance();
        AppState.Report currentReportPage = appState.getCurrentReportPage();

        ReportRecord selectedReport = ReportRecord.getSelectedReport();
        if(currentReportPage == AppState.Report.ADD){
            reportType.setItems(reportTypeList);
        }

        if(currentReportPage == AppState.Report.EDIT){
            reportType.setItems(reportTypeList);
            this.employeeIDField.setText(String.valueOf(selectedReport.getEmployeeID()));
            reportType.setValue(selectedReport.getReportType());
            reportTitleField.setText(selectedReport.getReportTitle());
            reportContentEditor.setHtmlText(selectedReport.getContent());
        }

        if(currentReportPage == AppState.Report.VIEW){
            WebEngine webEngine = reportContentView.getEngine();
            String content = selectedReport.getContent();
            System.out.println("HTML Content: " + content);
            // Retrieve necessary report details
            String reportTitle = selectedReport.getReportTitle();
            String reportType = selectedReport.getReportType();
            String reportContent = selectedReport.getContent(); // This is in HTML format
            String reportDate = selectedReport.getReportDate();
            String employeeName = selectedReport.getEmployeeName();

            // Reminders section
            String reminderMessage =
                    "<p><strong>Reminders:</strong></p>" +
                            "<ul>" +
                            "<li>This report is for internal use only. Ensure its contents are kept confidential and not disclosed <br>outside the organization.</li>" +
                            "<li>If any amendments are required, please contact the report creator promptly.</li>" +
                            "<li>Keep this document archived as it may be referenced for future audits or evaluations.</li>" +
                            "</ul>" +
                            "<p>Thank you for your continued commitment to maintaining high operational standards.</p>" +
                            "<p>Warm Regards,<br>The PAWFECTCare Administrative Team</p>";

            // Combine HTML content for the full report
            String fullReportContent = "<html><head><style>" +
                    "body { font-family: Times New Roman, Times, serif; line-height: 1.6; font-size: 12pt; width: 505px;}" + // Reduced overall font size
                    "h1 { color: #333; font-size: 12pt; margin-bottom: 10px; }" + // Smaller heading sizes
                    "h2 { color: #333; font-size: 10pt; margin-bottom: 8px; }" +
                    "h3 { color: #333; font-size: 9pt; margin-bottom: 6px; }" +
                    "p { margin: 5px 0; padding: 0; font-size: 8pt; }" +
                    "ul { margin: 5px 0; padding-left: 20px; font-size: 8pt; }" +
                    "</style></head><body contenteditable='false'>" +
                    "<h1>PAWFECTCare: Pet Grooming and Boarding Services</h1>" +
                    "<h2>Report</h2>" +
                    "<p><strong>Report Title:</strong> " + reportTitle + "</p>" +
                    "<p><strong>Report Type:</strong> " + reportType + "</p>" +
                    "<p><strong>Created By:</strong> " + employeeName + "</p>" +
                    "<p><strong>Submitted on:</strong> " + reportDate + "</p>" +
                    "<h3>Report Content</h3>" +
                    reportContent + // Include the HTML content of the report
                    reminderMessage +
                    "</body></html>";
            webEngine.loadContent(fullReportContent);
            webEngineForPrint.loadContent(fullReportContent);

            reportContentView.setOnScroll(event -> {
                if (event.isControlDown()) {
                    double zoomFactor = reportContentView.getZoom();
                    double deltaY = event.getDeltaY();
                    if (deltaY > 0) {
                        reportContentView.setZoom(zoomFactor * 1.1);
                    } else {
                        reportContentView.setZoom(zoomFactor / 1.1);
                    }
                    event.consume();
                }
            });
        }
    }

    public void addReport () throws IOException {
        try {
            // Validate inputs
            if(validateReportInputs()){
                return;
            }

            int employeeID = Integer.parseInt(employeeIDField.getText());
            String reportType = this.reportType.getValue();
            String reportTitle = this.reportTitleField.getText();
            String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert data into Payments table
            String query = "INSERT INTO Reports (EmployeeID, ReportType, ReportTitle, Content, ReportTimeStamp) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, employeeID);
            statement.setString(2, reportType);
            statement.setString(3, reportTitle);
            statement.setString(4, reportContent);
            statement.setTimestamp(5, timestamp);

            if(!(Alerts.showConfirmationDialog("Confirmation","Are you sure to add report? Please double check all fields."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the statement
            statement.close();
//            checkStatement.close();

            // Close the connection
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Report added successfully.");
                clearReportFields();
            } else {
                Alerts.showAlert("Error","Failed to add report. Please check your inputs.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding report.");
        }
    }

    private void clearReportFields() {
        employeeIDField.setText("");
        reportType.setValue("");
        reportTitleField.setText("");
        reportContentEditor.setHtmlText("");
    }

    public void saveReportChanges () throws IOException {
        try {
            if (validateReportInputs()) {
                return;
            }

            int employeeID = Integer.parseInt(employeeIDField.getText());
            String reportType = this.reportType.getValue();
            String reportTitle = this.reportTitleField.getText();
            String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();
            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            ReportRecord selectedReport = ReportRecord.getSelectedReport();
            if (selectedReport == null) {
                Alerts.showErrorDialog("Error", "No report selected.");
                return;
            }
            int selectedReportID = selectedReport.getReportID();

            String updateQuery = "UPDATE Reports SET EmployeeID = ?, ReportType = ?, ReportTitle = ?, Content = ?, ReportTimeStamp = ? WHERE ReportID = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(updateQuery)) {

                statement.setInt(1, employeeID);
                statement.setString(2, reportType);
                statement.setString(3, reportTitle);
                statement.setString(4, reportContent);
                statement.setTimestamp(5, timestamp);
                statement.setInt(6, selectedReportID);

                if(!(Alerts.showConfirmationDialog("Confirmation","Are you sure to update report? Please double check all changes."))){
                    return;
                }

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Report details updated successfully.");
                    clearReportFields();
                    NavigationController.switchToReportsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the report details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving report changes.");
        }
    }

    public void printReport() throws IOException {
        try {
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            if (printerJob != null && printerJob.showPrintDialog(null)) {
                boolean success = printerJob.printPage(webView);
                if (success) {
                    printerJob.endJob();
                } else {
                    Alerts.showErrorDialog("Error", "Printing failed.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while printing the report.");
        }
    }

    private boolean validateReportInputs() {
        String employeeID = employeeIDField.getText();
        String reportType = this.reportType.getValue();
        String reportTitle = this.reportTitleField.getText();
        String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();

        if (employeeID.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your Employee ID is required.");
            return true;
        }

        int employeeIDInt = 0;
        try {
            employeeIDInt = Integer.parseInt(employeeID); // Ensure PetID is a valid number
        } catch (NumberFormatException e) {
            Alerts.showAlert("Validation Error", "Employee ID must be a valid number.");
            return true;
        }

        if (!isEmployeeIDRegistered(employeeIDInt)) {
            Alerts.showAlert("Validation Error", "The provided EmployeeID is not registered in the system.");
            return true;
        }

        if (reportType.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report type is required.");
            return true;
        }

        if (reportTitle.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report title is required.");
            return true;
        }

        assert reportContent != null;
        if (reportContent.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report content is required.");
            return true;
        }

        int wordCount = countWords(reportContent);
        if (wordCount > 250) {
            Alerts.showAlert("Validation Error", "Report content exceeds the maximum allowed word limit of 250 words. Current word count: " + wordCount);
            return true;
        }

        return false;
    }

    private int countWords(String content) {
        if (content == null || content.isEmpty()) {
            return 0; // No words
        }

        // Strip HTML tags to ensure plain text word count
        String plainTextContent = content.replaceAll("<[^>]*>", ""); // Remove all HTML tags

        // Split by whitespace and count
        String[] words = plainTextContent.trim().split("\\s+");
        return words.length;
    }

    @FXML
    private void showRemainingWords(){
        String reportContent = reportContentEditor.getHtmlText() == null ? null : reportContentEditor.getHtmlText();
        int wordCount = countWords(reportContent);
        int remainingWords = 250 - wordCount;
        remainingWordsLabel.setText("Remaining words: " + remainingWords);
    }

    private boolean isEmployeeIDRegistered(int employeeID) {
        String query = "SELECT COUNT(*) FROM Employees WHERE EmployeeID = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, employeeID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the reservation exists
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error", "Failed to validate EmployeeID.");
        }

        return false; // If something goes wrong or no matching reservation is found
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
