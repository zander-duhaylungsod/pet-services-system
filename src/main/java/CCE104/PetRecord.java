package CCE104;

import java.sql.*;

public class PetRecord {
    private int petID;
    private String name;
    private String species;
    private String breed;
    private int age;
    private int ownerID;
    private String petImagePath;
    private String ownerName;
    private String petNotes;
    private String petCount;

    // Database credentials
    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

    // Constructor, Getters, and Setters
    public PetRecord(int petID, String name, String species, String breed, int age, int ownerID, String ownerName, String petNotes, String petImagePath) {
        this.petID = petID;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.petNotes = petNotes;
        this.petImagePath = petImagePath;
    }

    // getters and setters...
    public int getPetID() { return petID; }

    public void setPetID(int petID) { this.petID = petID; }

    public int getOwnerID() { return ownerID; }

    public String getName() {return name;}

    public String setPetCount() {
        return petCount;
    }

    public String getPetCount() {
        String query = "SELECT COUNT(*) AS PetCount FROM Pets;";
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        petCount = rs.getString("PetCount");
                        return petCount;
                    } else {
                        Alerts.showAlert("Error", "Cannot find registered pets.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alerts.showAlert("Error", "An error occurred while counting registered Pets.");
            }
        return "";
    }

    public String getSpecies() {return species;}
    public String getBreed() {return breed;}
    public String getOwnerName() {return ownerName;}
    public String getPetImagePath() {return petImagePath;}
    public String getPetNotes() {return petNotes;}
    public int getPetAge() {return age;}


    private static PetRecord instance;
    private RecordsController recordsController;
    private static PetRecord selectedPet;

    public static PetRecord getSelectedPet() {
        return selectedPet;
    }

    public static void setSelectedPet(PetRecord pet) {
        selectedPet = pet;
    }

    private PetRecord() {}

    public static PetRecord getInstance() {
        if (instance == null) {
            instance = new PetRecord();
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
