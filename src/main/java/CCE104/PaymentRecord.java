package CCE104;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentRecord {
    private int paymentID;
    private int employeeID;
    private String employeeName;
    private int ownerID;
    private String ownerName;
    private int petID;
    private String petName;
    private double amount;
    private double remainingAmount;
    private String paymentDate;
    private String method;
    private String service;
    private int appointmentID;
    private String status;

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(PaymentRecord.class.getName());

    // Constructor
    public PaymentRecord(int paymentID, String petName, String ownerName, String service, String paymentDate, double amount, String status) {
        this.paymentID = paymentID;
        this.petName = petName;
        this.ownerName = ownerName;
        this.service = service;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public int getPetID() {
        String queryForAppointment = "SELECT PetID FROM Appointments WHERE AppointmentID = ?";
        String queryForReservation = "SELECT PetID FROM BoardingReservations WHERE ReservationID = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (selectedPayment.getAppointmentID() != 0) {
                try (PreparedStatement stmt = connection.prepareStatement(queryForAppointment)) {
                    stmt.setInt(1, selectedPayment.getAppointmentID());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("PetID");
                        } else {
                            Alerts.showAlert("Error", "No matching appointment found for the given AppointmentID.");
                        }
                    }
                }
            } else if (selectedPayment.getReservationID() != 0) {
                try (PreparedStatement stmt = connection.prepareStatement(queryForReservation)) {
                    stmt.setInt(1, selectedPayment.getReservationID());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("PetID");
                        } else {
                            Alerts.showAlert("Error", "No matching reservation found for the given ReservationID.");
                        }
                    }
                }
            } else {
                Alerts.showAlert("Error", "Neither AppointmentID nor ReservationID is set for this payment.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the PetID.");
        }
        return petID;
    }
    public void setPetID(int petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }
    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getEmployeeID() {
        String query = "SELECT e.EmployeeID, CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName FROM Payments p JOIN Employees e ON p.EmployeeID = e.EmployeeID WHERE p.PaymentID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employeeID = rs.getInt("EmployeeID");
                    employeeName = rs.getString("EmployeeName");
                    return employeeID;
                } else {
                    Alerts.showAlert("Error", "No matching record found for the given PaymentID.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the EmployeeID and EmployeeName.");
        }
        return -1;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        getEmployeeID();
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getOwnerID() {
        String query = "SELECT OwnerID FROM Owners WHERE FirstName = ? AND LastName = ?";
        String[] names = ownerName.split(" ");
        if (names.length == 2) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, names[0]);
                stmt.setString(2, names[1]);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ownerID = rs.getInt("OwnerID");
                        return ownerID;
                    } else {
                        Alerts.showAlert("Error", "No matching owner found for the given OwnerName.");
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
                Alerts.showAlert("Error", "An error occurred while retrieving the OwnerID.");
            }
        } else {
            Alerts.showAlert("Error", "OwnerName should contain exactly two parts: first and last name.");
        }
        return -1;
    }
    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getPaymentID() {
        return paymentID;
    }
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getService() {
        if ("Boarding".equalsIgnoreCase(service)) {
            return "Boarding";
        } else {
            String query = "SELECT s.ServiceName FROM Payments p " +
                    "JOIN Appointments a ON p.AppointmentID = a.AppointmentID " +
                    "JOIN Services s ON a.ServiceID = s.ServiceID " +
                    "WHERE p.PaymentID = ?";
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, paymentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("ServiceName");
                    } else {
                        Alerts.showAlert("Error", "No matching service found for the given PaymentID.");
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
                Alerts.showAlert("Error", "An error occurred while retrieving the ServiceName.");
            }
        }
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    private void calculateRemainingAmount() {
        PaymentRecord selectedPayment = PaymentRecord.getSelectedPayment();

        if (selectedPayment == null) {
            System.out.println("No payment selected.");
            return;
        }

        double totalPaid = 0.0;
        double totalCost = 0.0;

        // Fetch existing payments associated with AppointmentID or ReservationID
        if (selectedPayment.getAppointmentID() != 0) {
            // Calculate total paid amount for the same AppointmentID
            totalPaid = PaymentController.fetchTotalPaidByAppointment(selectedPayment.getAppointmentID());
            totalCost = PaymentController.fetchServicePriceByAppointment(selectedPayment.getAppointmentID());
        } else if (selectedPayment.getReservationID() != 0) {
            // Calculate total paid amount for the same ReservationID
            totalPaid = PaymentController.fetchTotalPaidByReservation(selectedPayment.getReservationID());
            totalCost = PaymentController.calculateTotalCost(selectedPayment.getReservationID());
        }

        // Calculate remaining amount
        remainingAmount = totalCost - totalPaid;

        // Debugging/Logging
        System.out.println("Total Cost: " + totalCost);
        System.out.println("Total Paid: " + totalPaid);
        System.out.println("Remaining Amount: " + remainingAmount);
    }

    public double getRemainingAmount() {
        calculateRemainingAmount();
        return remainingAmount;
    }
    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMethod() {
        String query = "SELECT Method FROM Payments WHERE PaymentID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Method");
                } else {
                    Alerts.showAlert("Error", "No matching record found for the given PaymentID.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the Method.");
        }
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public int getAppointmentID() {
        String query = "SELECT AppointmentID FROM Payments WHERE PaymentID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AppointmentID");
                } else {
                    Alerts.showAlert("Error", "No matching record found for the given PaymentID.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQLException occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the AppointmentID.");
        }
        return -1;
    }
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getReservationID() {
        String query = "SELECT ReservationID FROM Payments WHERE PaymentID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ReservationID");
                } else {
                    Alerts.showAlert("Error", "No matching record found for the given PaymentID.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
            Alerts.showAlert("Error", "An error occurred while retrieving the ReservationID.");
        }
        return -1;
    }
    public void setReservationID(int reservationID) { this.appointmentID = reservationID; }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Singleton Instance and Selection Handling
    private static PaymentRecord instance;
    private static PaymentRecord selectedPayment;
    private ReportsPageController reportsPageController;

    public static PaymentRecord getSelectedPayment() {
        return selectedPayment;
    }
    public static void setSelectedPayment(PaymentRecord payment) {
        selectedPayment = payment;
    }

    private PaymentRecord() {}

    public static PaymentRecord getInstance() {
        if (instance == null) {
            instance = new PaymentRecord();
        }
        return instance;
    }

    public ReportsPageController getReportsPageController() {
        return reportsPageController;
    }
    public void setReportsPageController(ReportsPageController reportsPageController) {
        this.reportsPageController = reportsPageController;
    }

    @Override
    public String toString() {
        return "PaymentRecord {" +
                "paymentID=" + paymentID +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", method='" + method + '\'' +
                ", appointmentID=" + appointmentID +
                ", status='" + status + '\'' +
                '}';
    }
}
