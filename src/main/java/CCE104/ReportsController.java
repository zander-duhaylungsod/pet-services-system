package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

import javafx.print.*;

//import java.awt.*;
//import java.awt.print.PageFormat;
//import java.awt.print.Printable;
//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private TextField searchField;
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

    // Database credentials
    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

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
                String fullHtmlDocument = "<html><head><style>" +
                        "body { font-family: Arial, sans-serif; line-height: 1; }" +
                        "h1, h2 { color: #333; }" +
                        "p { margin: 0; padding: 0; }" +
                        "ul { margin: 10px 0; padding-left: 20px; }" +
                        "</style></head><body>" +
                        selectedReport.getContent() +
                        "</body></html>";
                webEngine.loadContent(fullHtmlDocument);
        }
    }

    public void addReport () throws IOException {
        try {
            // Validate inputs
            if(!validateReportInputs()){
                return;
            }

            int employeeID = Integer.parseInt(employeeIDField.getText());
            String reportType = this.reportType.getValue();
            String reportTitle = this.reportTitleField.getText();
            String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            Connection connection = DriverManager.getConnection(url, user, password);

            // Insert data into Payments table
            String query = "INSERT INTO Reports (EmployeeID, ReportType, ReportTitle, Content, ReportDate) VALUES (?, ?, ?, ?, ?);";
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
            e.printStackTrace();
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
            if (!validateReportInputs()) {
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
            if (selectedReport == null) {
                Alerts.showErrorDialog("No Report Selected", "Please select a report to edit.");
                return;
            }

            String updateQuery = "UPDATE Reports SET EmployeeID = ?, ReportType = ?, ReportTitle = ?, Content = ?, ReportDate = ? WHERE ReportID = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
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
                    Main.switchSceneWithFade("scenes/reportsAdmin");
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the report details.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showErrorDialog("Error", "An error occurred while saving report changes.");
        }
    }

    public void printReport() throws IOException {
        try {
            ReportRecord selectedReport = ReportRecord.getSelectedReport();
            if (selectedReport == null) {
                Alerts.showErrorDialog("Error", "No report selected.");
                return;
            }

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
                            "<li>This report is for internal use only. Ensure its contents are kept confidential and not disclosed</li>" +
                            "<li>outside the organization.</li>" +
                            "<li>If any amendments are required, please contact the report creator promptly.</li>" +
                            "<li>Keep this document archived as it may be referenced for future audits or evaluations.</li>" +
                            "</ul>" +
                            "<p>Thank you for your continued commitment to maintaining high operational standards.</p>" +
                            "<p>Warm Regards,<br>The PAWFECTCare Administrative Team</p>";

            // Combine HTML content for the full report
            String fullReportContent = "<html><head><style>" +
                    "body { font-family: Arial, sans-serif; line-height: 1.6; font-size: 10pt; }" + // Reduced overall font size
                    "h1 { color: #333; font-size: 12pt; margin-bottom: 10px; }" + // Smaller heading sizes
                    "h2 { color: #333; font-size: 10pt; margin-bottom: 8px; }" +
                    "h3 { color: #333; font-size: 9pt; margin-bottom: 6px; }" +
                    "p { margin: 5px 0; padding: 0; font-size: 8pt; }" +
                    "ul { margin: 5px 0; padding-left: 20px; font-size: 8pt; }" +
                    "</style></head><body>" +
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

            // Use WebView to render the HTML
            WebView webView = new WebView();
            webView.getEngine().loadContent(fullReportContent);

            // Print the WebView content
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
            e.printStackTrace();
            Alerts.showErrorDialog("Error", "An error occurred while printing the report.");
        }
    }


//    public void printReport () throws IOException {
//        try {
//            ReportRecord selectedReport = ReportRecord.getSelectedReport();
//            // Collect all necessary details for the printout
//            if (selectedReport == null) {
//                Alerts.showErrorDialog("Error", "No report selected.");
//                return;
//            }
//
//            // Collect information for the printout
//            ReportsPageController reportsPageController = ReportRecord.getInstance().getReportsPageController();
//            Integer selectedReportID = reportsPageController.getSelectedReportID();
//
//            String employeeID = employeeIDField.getText();
//            String reportType = this.reportType.getValue();
//            String reportTitle = this.reportTitleField.getText();
//            String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();
//            String reportDate = selectedReport.getReportDate();
//
//            String employeeName = selectedReport.getEmployeeName();
//            String employeeRole = selectedReport.getEmployeeRole();
//
//            // Create the printable content
//            String reminderMessage =
//                    "\n-------------------------------------- Reminder -----------------------------------\n" +
//                            "Reminders:\n" +
//                            "- This report is for internal use only. Ensure its contents are kept confidential \nand not disclosed outside the organization.\n" +
//                            "- If any amendments are required, please contact the report creator promptly.\n" +
//                            "- Keep this document archived as it may be referenced for future audits or evaluations.\n\n" +
//                            "For inquiries or assistance, reach out to the creator directly or contact the administrative \nteam through the provided channels.\n\n" +
//                            "Thank you for your continued commitment to maintaining high operational standards.\n\n" +
//                            "Warm Regards,\n" +
//                            "The PAWFECTCare Administrative Team\n" +
//                            "-----------------------------------------------------------------------------------------";
//
//            String printContent =
//                    "--------------PAWFECTCare: Pet Grooming and Boarding Services--------------\n" +
//                    "---------------------------------Report------------------------------------\n" +
//                            "The following report has been successfully generated as part of the regular operational \nand administrative duties carried out by our dedicated staff:\n\n" +
//                            "Report Title: " + reportTitle + "\n" +
//                            "Report Type: " + reportType + "\n" +
//                            "Created By: " + employeeName + "\n" +
//                            "Employee ID: " + employeeID + "\n" +
//                            "Submitted in: " + reportDate + "\n" +
//                            "Report Content: " + "\n" +
//                             reportContent + "\n\n" +
//                            reminderMessage;
//
//            // Create a PrinterJob instance and set up the print settings
//            PrinterJob printerJob = PrinterJob.getPrinterJob();
//
//            // Set a print job (we can use a simple text-based print job)
//            printerJob.setPrintable(new Printable() {
//                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//                    if (pageIndex >= 1) { // Only 1 page to print
//                        return Printable.NO_SUCH_PAGE;
//                    }
//
//                    // Graphics object used to render the content
//                    Graphics2D g2d = (Graphics2D) graphics;
//                    g2d.setFont(new Font("Serif", Font.PLAIN, 12));
//                    g2d.setColor(java.awt.Color.BLACK);
//
//                    // Adjust the page's print area and draw the content
//                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//
//                    // Split the content into lines
//                    String[] lines = printContent.split("\n");
//                    int yPosition = 100; // Starting y position for the first line
//
//                    // Iterate over the lines and print each one
//                    for (String line : lines) {
//                        g2d.drawString(line, 100, yPosition); // Print each line at the new y position
//                        yPosition += 15; // Increment y position for the next line (line height)
//                    }
//
//                    return Printable.PAGE_EXISTS;
//                }
//            });
//
//            // Show the print dialog to the user
//            if (printerJob.printDialog()) {
//                printerJob.print();  // Print the document
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Alerts.showErrorDialog("Error", "An error occurred while printing payment details.");
//        }
//    }

    private boolean validateReportInputs() {
        String employeeID = employeeIDField.getText();
        String reportType = this.reportType.getValue();
        String reportTitle = this.reportTitleField.getText();
        String reportContent = reportContentEditor.getHtmlText() == null? null:reportContentEditor.getHtmlText();

        if (employeeID.isEmpty()) {
            Alerts.showAlert("Validation Error", "Your Employee ID is required.");
            return false;
        }

        int employeeIDInt = 0;
        try {
            employeeIDInt = Integer.parseInt(employeeID); // Ensure PetID is a valid number
        } catch (NumberFormatException e) {
            Alerts.showAlert("Validation Error", "Employee ID must be a valid number.");
            return false;
        }

        if (!isEmployeeIDRegistered(employeeIDInt)) {
            Alerts.showAlert("Validation Error", "The provided EmployeeID is not registered in the system.");
            return false;
        }

        if (reportType.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report type is required.");
            return false;
        }

        if (reportTitle.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report title is required.");
            return false;
        }

        if (reportContent.isEmpty()) {
            Alerts.showAlert("Validation Error", "Report content is required.");
            return false;
        }

        return true;
    }

    private boolean isEmployeeIDRegistered(int employeeID) {
        String query = "SELECT COUNT(*) FROM Employees WHERE EmployeeID = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, employeeID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the reservation exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    @FXML
    public void searchFunction(KeyEvent event) {
        //add search function here
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

