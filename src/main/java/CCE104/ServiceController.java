package CCE104;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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

    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

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
            if(!validateServiceInputs()){
                return;
            }

            String name = serviceName.getText().trim();
            Double price = Double.parseDouble(servicePrice.getText().trim());
            String description = serviceDescription.getText().trim();

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            String query = "INSERT INTO Services (ServiceName, Price, Description) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, description);

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                showSuccessDialog("Success", "Service added successfully.");
                clearServiceFields();
            } else {
                showAlert("Error","Failed to add service.");
            }
        } catch (IllegalArgumentException e) {
            showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while adding service.");
        }
    }

    @FXML
    public void saveServiceChanges(ActionEvent event) {
        try {
            if(!validateServiceInputs()){
                return;
            }

            String name = serviceName.getText().trim();
            Double price = Double.parseDouble(servicePrice.getText().trim());
            String description = serviceDescription.getText().trim();

            //Get selected ID
            RecordsController recordsController = ServiceRecord.getInstance().getRecordsController();
            Integer selectedServiceID = recordsController.getSelectedServiceID();
            if (selectedServiceID == null) {
                showErrorDialog("No Service Selected", "Please select a service to edit.");
                return;
            }

            //Update changes in the DB
            String updateQuery = "UPDATE Services SET ServiceName = ?, Price = ?, Description = ? WHERE ServiceID = ?";
            try (Connection conn = DriverManager.getConnection(url,user,password);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setString(3, description);
                stmt.setInt(4, selectedServiceID);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showSuccessDialog("Success", "Service details updated successfully.");
                    Main.switchSceneWithFade("scenes/recordsAdmin");
                } else {
                    showErrorDialog("Update Failed", "No changes were made to the service details.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while saving service changes.");
        }
    }

    @FXML
    public void printService(ActionEvent event) {
        //add function here
    }

    //validators & cleaners
    private boolean validateServiceInputs() {
        String name = serviceName.getText().trim();
        String price = servicePrice.getText().trim();
        String description = serviceDescription.getText().trim();

        if (name.isEmpty()) {
            showAlert("Validation Error", "Service name is required.");
            return false;
        }

        if (price.isEmpty()) {
            showAlert("Validation Error", "Service Price is required.");
            return false;
        }

        if (description.isEmpty()) {
            showAlert("Validation Error", "Service Description is required.");
            return false;
        }

        return true;
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

    @FXML
    public void searchFunction(KeyEvent event) {
        //add search function here
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

    //alerts
    public static void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null);
        alert.setContentText(message); alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
