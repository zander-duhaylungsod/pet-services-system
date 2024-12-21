package CCE104;

import javafx.util.Pair;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentRecord {
    private int appointmentID;
    private Date date;
    private Time time;
    private int serviceID = -1;
    private String serviceName;
    private String petName;
    private int petID;
    private String ownerName;
    private int ownerID;
    private int employeeID;
    private int branchID;
    private String status;
    private double totalCost;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(AppointmentRecord.class.getName());

    // Constructor
    public AppointmentRecord(int appointmentID,  String petName, String ownerName, Date date, Time time, String serviceName, String status) {
        this.appointmentID = appointmentID;
        this.petName = petName;
        this.ownerName = ownerName;
        this.date = date;
        this.time = time;
        this.serviceName = serviceName;
        this.status = status;
    }

    // Getters and Setters
    public int getAppointmentID() { return appointmentID; }
    public void setAppointmentID(int appointmentID) { this.appointmentID = appointmentID; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    public int getServiceID(String serviceName) {
        // Database connection information
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = ""; // Replace with your password

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT ServiceID, ServiceName FROM Services WHERE ServiceName = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, serviceName);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        serviceID = rs.getInt("ServiceID"); // Retrieve serviceID
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error","An error occured while retrieving the ServiceID.");
        }
        return serviceID;
    }
    public void setServiceID(int serviceID) { this.serviceID = serviceID; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public int getPetID(String petName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT PetID, Name FROM Pets WHERE Name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, petName);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        petID = rs.getInt("PetID");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error","An error occured while retrieving the PetID.");
        }
        return petID;
    }
    public void setPetID(int petID) { this.petID = petID; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public int getOwnerID() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to get the OwnerID by matching ownerFirstName, ownerLastName, and petID
            String query = "SELECT o.OwnerID " +
                    "FROM Owners o " +
                    "JOIN Pets p ON o.OwnerID = p.OwnerID " +
                    "WHERE o.FirstName = ? AND o.LastName = ? AND p.PetID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                // Split ownerName into FirstName and LastName
                String[] nameParts = ownerName.split(" ", 2);
                if (nameParts.length < 2) {
                    Alerts.showAlert("Error", "Owner name is incomplete.");
                    return -1;
                }

                String firstName = nameParts[0];
                String lastName = nameParts[1];

                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setInt(3, selectedAppointment.getPetID(selectedAppointment.getPetName()));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ownerID = rs.getInt("OwnerID");
                    } else {
                        Alerts.showAlert("Error", "No matching OwnerID found.");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the OwnerID.");
        }
        return ownerID;
    }
    public void setOwnerID(int ownerID) { this.ownerID = ownerID; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalCost() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            fetchServicePrice(serviceName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error","An error occured while retrieving the service's price.");
        }
        return totalCost;
    }
    public double getTotalCost(String serviceName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            fetchServicePrice(serviceName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error","An error occured while retrieving the service's price.");
        }
        return totalCost;
    }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    // Fetch price for the serviceID and set totalCost
    public void fetchServicePrice(String serviceName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int sID = getServiceID(serviceName);
            // Query to get the service price by serviceID
            String query = "SELECT Price FROM Services WHERE ServiceID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, sID);
            ResultSet rs = stmt.executeQuery();

            // If the result exists, set the totalCost
            if (rs.next()) {
                this.totalCost = rs.getDouble("Price");
            } else {
                Alerts.showAlert("Error","Service not found for ServiceID: " + sID);
                this.totalCost = 0.0; // If no service found, set default 0
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            this.totalCost = 0.0; // If there's an error, set default 0
        }
    }

    // Singleton Instance and Selection Handling
    private static AppointmentRecord instance;
    private static AppointmentRecord selectedAppointment;
    private RecordsController recordsController;

    public static AppointmentRecord getSelectedAppointment() { return selectedAppointment; }
    public static void setSelectedAppointment(AppointmentRecord appointment) { selectedAppointment = appointment; }
    private AppointmentRecord() {}

    public static AppointmentRecord getInstance() {
        if (instance == null) {
            instance = new AppointmentRecord();
        }
        return instance;
    }

    public RecordsController getRecordsController() { return recordsController; }
    public void setRecordsController(RecordsController recordsController) { this.recordsController = recordsController; }
}
