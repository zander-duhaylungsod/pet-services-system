package CCE104;

public class ServiceRecord {
    private int serviceID;
    private String serviceName;
    private double price;
    private String description;

    // Constructor
    public ServiceRecord(int serviceID, String serviceName, double price, String description) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Singleton Instance and Selection Handling
    private static ServiceRecord instance;
    private static ServiceRecord selectedService;
    private RecordsController recordsController;

    public static ServiceRecord getSelectedService() {
        return selectedService;
    }

    public static void setSelectedService(ServiceRecord service) {
        selectedService = service;
    }

    private ServiceRecord() {}

    public static ServiceRecord getInstance() {
        if (instance == null) {
            instance = new ServiceRecord();
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
