package CCE104;

import java.sql.*;

public class AppointmentRecord {
    private int appointmentID;
    private Date date;
    private Time time;
    private int serviceID;
    private String serviceName;
    private String petName;
    private int petID;
    private String ownerName;
    private int ownerID;
    private int employeeID;
    private int branchID;
    private String status;
    private double totalCost;

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

//    public AppointmentRecord(int appointmentID, Date date, Time time, String serviceName, String petName, int ownerID, String ownerName, int employeeID, String status) {
//        this.appointmentID = appointmentID;
//        this.date = date;
//        this.time = time;
//        this.serviceName = serviceName;
//        this.petID = petID;
//        this.petName = petName;
//        this.ownerID = ownerID;
//        this.ownerName = ownerName;
//        this.employeeID = employeeID;
//        this.status = status;
//    }

    // Getters and Setters
    public int getAppointmentID() { return appointmentID; }
    public void setAppointmentID(int appointmentID) { this.appointmentID = appointmentID; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    public int getServiceID() { return serviceID; }
    public void setServiceID(int serviceID) { this.serviceID = serviceID; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public int getPetID() { return petID; }
    public void setPetID(int petID) { this.petID = petID; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public int getOwnerID() { return ownerID; }
    public void setOwnerID(int ownerID) { this.ownerID = ownerID; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    // Fetch price for the serviceID and set totalCost
    public void fetchServicePrice(Connection conn) {
        try {
            // Query to get the service price by serviceID
            String query = "SELECT Price FROM Services WHERE ServiceID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, this.serviceID);
            ResultSet rs = stmt.executeQuery();

            // If the result exists, set the totalCost
            if (rs.next()) {
                this.totalCost = rs.getDouble("Price");
            } else {
                System.out.println("Service not found for ServiceID: " + this.serviceID);
                this.totalCost = 0.0; // If no service found, set default 0
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            this.totalCost = 0.0; // If there's an error, set default 0
        }
    }

    // Singleton Instance and Selection Handling
    private static AppointmentRecord instance;
    private static AppointmentRecord selectedAppointment;
    private RecordsController recordsController;

    public static AppointmentRecord getSelectedAppointment() {
        return selectedAppointment;
    }
    public static void setSelectedAppointment(AppointmentRecord appointment) {
        selectedAppointment = appointment;
    }
    private AppointmentRecord() {}

    public static AppointmentRecord getInstance() {
        if (instance == null) {
            instance = new AppointmentRecord();
        }
        return instance;
    }

    public RecordsController getRecordsController() {
        return recordsController;
    }
    public void setRecordsController(RecordsController recordsController) {
        this.recordsController = recordsController;
    }
}
