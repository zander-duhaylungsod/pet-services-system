package CCE104;

public class PetRecord {
    private int ownerID;
    private String ownerName;
    private int petID;
    private String petName;

    public PetRecord(int ownerID, String ownerName, int petID, String petName) {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.petID = petID;
        this.petName = petName;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getPetID() {
        return petID;
    }

    public String getPetName() {
        return petName;
    }
}
