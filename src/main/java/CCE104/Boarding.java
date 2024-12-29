package CCE104;

public class Boarding {
    private final String ownerName;
    private final String petName;
    private final String startDate;

    // Constructor
    public Boarding(String ownerName, String petName, String startDate) {
        this.ownerName = ownerName;
        this.petName = petName;
        this.startDate = startDate;
    }

    // Getters
    public String getOwnerName() {
        return ownerName;
    }

    public String getPetName() {
        return petName;
    }

    public String getStartDate() {
        return startDate;
    }
}
