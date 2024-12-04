package CCE104;

public class OwnerRecord {
    private int ownerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // Constructor
    public OwnerRecord(int ownerID, String firstName, String lastName, String email, String phone) {
        this.ownerID = ownerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Singleton Instance and Selection Handling
    private static OwnerRecord instance;
    private static OwnerRecord selectedOwner;
    private RecordsController recordsController;

    public static OwnerRecord getSelectedOwner() {
        return selectedOwner;
    }

    public static void setSelectedOwner(OwnerRecord owner) {
        selectedOwner = owner;
    }

    private OwnerRecord() {}

    public static OwnerRecord getInstance() {
        if (instance == null) {
            instance = new OwnerRecord();
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
