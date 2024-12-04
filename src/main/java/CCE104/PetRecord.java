package CCE104;

public class PetRecord {
    private int petID;
    private String name;
    private String species;
    private String breed;
    private int age;
    private int ownerID;
    private String petImagePath;
    private String ownerName;

    // Constructor, Getters, and Setters
    public PetRecord(int petID, String name, String species, String breed, int age, int ownerID, String ownerName, String petImagePath) {
        this.petID = petID;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.petImagePath = petImagePath;
    }

    // getters and setters...
    public int getPetID() { return petID; }

    public void setPetID(int petID) { this.petID = petID; }

    public int getOwnerID() { return ownerID; }

    public String getName() {return name;}
    public String getSpecies() {return species;}
    public String getBreed() {return breed;}
    public String getOwnerName() {return ownerName;}
    public String getPetImagePath() {return petImagePath;}
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
