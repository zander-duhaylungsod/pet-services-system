package CCE104;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Label employeeName;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TextArea serviceDescription;
    @FXML
    private TextField serviceName;
    @FXML
    private TextField servicePrice;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(ServiceController.class.getName());

    public void initialize() {
        AppState appState = AppState.getInstance();
        AppState.Service currentServicePage = appState.getCurrentServicePage();
        ServiceRecord selectedService = ServiceRecord.getSelectedService();

        if (currentServicePage != AppState.Service.ADD) {
            if (selectedService != null) {
                serviceName.setText(selectedService.getServiceName());
                servicePrice.setText(String.valueOf(selectedService.getPrice()));
                serviceDescription.setText(selectedService.getDescription());
            }
        }
    }

    @FXML
    public void addService(ActionEvent event) {
        try {
            // Validate inputs
            if(validateServiceInputs()){
                return;
            }

            String name = serviceName.getText().trim();
            double price = Double.parseDouble(servicePrice.getText().trim());
            String description = serviceDescription.getText().trim();

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "INSERT INTO Services (ServiceName, Price, Description) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, description);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add service? Please double check fields."))){
                return;
            }
            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                Alerts.showSuccessDialog("Success", "Service added successfully.");
                clearServiceFields();
            } else {
                Alerts.showAlert("Error","Failed to add service.");
            }
        } catch (IllegalArgumentException e) {
            Alerts.showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while adding service.");
        }
    }

    @FXML
    public void saveServiceChanges(ActionEvent event) {
        try {
            if(validateServiceInputs()){
                return;
            }

            String name = serviceName.getText().trim();
            double price = Double.parseDouble(servicePrice.getText().trim());
            String description = serviceDescription.getText().trim();

            //Get selected ID
            RecordsController recordsController = ServiceRecord.getInstance().getRecordsController();
            Integer selectedServiceID = recordsController.getSelectedServiceID();
            if (selectedServiceID == null) {
                Alerts.showErrorDialog("No Service Selected", "Please select a service to edit.");
                return;
            }

            //Update changes in the DB
            String updateQuery = "UPDATE Services SET ServiceName = ?, Price = ?, Description = ? WHERE ServiceID = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setString(3, description);
                stmt.setInt(4, selectedServiceID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save service changes? Please double check fields."))){
                    return;
                }
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    Alerts.showSuccessDialog("Success", "Service details updated successfully.");
                    NavigationController.switchToRecordsWithFade();
                } else {
                    Alerts.showErrorDialog("Update Failed", "No changes were made to the service details.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showErrorDialog("Error", "An error occurred while saving service changes.");
        }
    }

    //validators & cleaners
    private boolean validateServiceInputs() {
        String name = serviceName.getText().trim();
        String price = servicePrice.getText().trim();
        String description = serviceDescription.getText().trim();

        if (name.isEmpty()) {
            Alerts.showAlert("Validation Error", "Service name is required.");
            return true;
        }

        if (price.isEmpty()) {
            Alerts.showAlert("Validation Error", "Service Price is required.");
            return true;
        }

        if (description.isEmpty()) {
            Alerts.showAlert("Validation Error", "Service Description is required.");
            return true;
        }

        return false;
    }

    private void clearServiceFields () {
        serviceName.setText("");
        servicePrice.setText("");
        serviceDescription.setText("");
    }

    //transitions & effects
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
        NavigationController.logOut();
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
