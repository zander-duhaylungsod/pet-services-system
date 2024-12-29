package CCE104;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardingRecord {
    private int reservationID;
    private Date startDate;
    private Date endDate;
    private int petID;
    private int employeeID;
    private String petName;
    private String petNotes;
    private String ownerName;
    private int ownerID;
    private String status;
    private double totalCost;
    public static String capacity;

    //logger
    private static final Logger LOGGER = Logger.getLogger(BoardingController.class.getName());

    // Constructor
    public BoardingRecord(int reservationID, String petName, String ownerName, Date startDate, Date endDate, String status) {
        this.reservationID = reservationID;
        this.petName = petName;
        this.ownerName = ownerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getReservationID() { return reservationID; }
    public void setReservationID(int reservationID) { this.reservationID = reservationID; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getPetID() {
        fetchPetAndOwnerIDs(selectedBoarding.getPetName(), selectedBoarding.getOwnerName());
        return petID;
    }
    public void setPetID(int petID) { this.petID = petID; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPetNotes() {
        fetchPetAndOwnerIDs(selectedBoarding.getPetName(), selectedBoarding.getOwnerName());
        String query = "SELECT PetNotes FROM Pets WHERE PetID = ?;";
        int maxLength = 50; // Maximum length per line

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, String.valueOf(petID));

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Fetch the PetNotes value
                String fetchedNotes = resultSet.getString("PetNotes");

                // Format the string to fit the maxLength
                petNotes = formatStringWithLineBreaks(fetchedNotes, maxLength);
            } else {
                Alerts.showAlert("Error", "No records found for petName and ownerName combination.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }

        return petNotes;
    }

    private String formatStringWithLineBreaks(String input, int maxLength) {
        StringBuilder formatted = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i += maxLength) {
            // Append a substring of maxLength or remaining length
            int end = Math.min(i + maxLength, length);
            formatted.append(input, i, end).append("\n");
        }

        return formatted.toString().trim(); // Remove trailing newline
    }
    public void setPetNotes(String petNotes) { this.petNotes = petNotes; }

    public int getOwnerID() {
        fetchPetAndOwnerIDs(selectedBoarding.getPetName(), selectedBoarding.ownerName);
        return ownerID;
    }
    public void setOwnerID(int ownerID) { this.ownerID = ownerID; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Static Instance for Singleton pattern
    private static BoardingRecord instance;
    private RecordsController recordsController;
    public static BoardingRecord selectedBoarding;

    public static BoardingRecord getInstance() {
        if (instance == null) {
            instance = new BoardingRecord();
        }
        return instance;
    }

    public RecordsController getRecordsController() { return recordsController; }
    public void setRecordsController(RecordsController recordsController) { this.recordsController = recordsController; }

    // Private constructor for singleton
    private BoardingRecord() {}

    public static BoardingRecord getSelectedBoarding() { return selectedBoarding; }

    public static void setSelectedBoarding(BoardingRecord boarding) { selectedBoarding = boarding; }

    public void fetchPetAndOwnerIDs(String petName, String ownerName) {
        // Split ownerName into FirstName and LastName
        String[] nameParts = ownerName.split(" ", 2);
        if (nameParts.length < 2) {
            Alerts.showAlert("Error", "Owner name is incomplete.");
        }

        String firstName = nameParts[0];
        String lastName = nameParts[1];

        String query = "SELECT p.PetID, o.OwnerID FROM Pets p "
                + "JOIN Owners o ON p.OwnerID = o.OwnerID "
                + "WHERE p.Name = ? AND o.FirstName = ? AND o.LastName = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, petName);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Fetch petID and ownerID from the result set
                this.petID = resultSet.getInt("PetID");
                this.ownerID = resultSet.getInt("OwnerID");
            } else {
                Alerts.showAlert("Error","No records found for petName and ownerName combination.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public int getActiveReservationsCountFromDB() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM BoardingReservations WHERE Status NOT IN ('Cancelled', 'Completed', 'Pending')")) {

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);  // Returns the count of active reservations
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }

        return 0;  // Return 0 if there was an error or no active reservations
    }

    public int getRemainingCapacity() {
        int activeReservations = getActiveReservationsCountFromDB();
        int maxCapacity = 20;
        return maxCapacity - activeReservations;
    }

    private Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
