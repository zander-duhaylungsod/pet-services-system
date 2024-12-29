package CCE104;

public class Appointment {
    private final String ownerName;
    private final String petName;
    private final String serviceName;
    private final String appointmentDate;

    // Constructor
    public Appointment(String ownerName, String petName, String serviceName, String appointmentDate) {
        this.ownerName = ownerName;
        this.petName = petName;
        this.serviceName = serviceName;
        this.appointmentDate = appointmentDate;
    }

    // Getters
    public String getOwnerName() {
        return ownerName;
    }

    public String getPetName() {
        return petName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }
}
