package CCE104;

public class AppState {

    // Singleton instance
    private static AppState instance;
    private int currentTabIndex = 0;

    // Method to get the instance of the AppState
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public enum Page {
        DASHBOARD,
        RECORDS,
        REPORTS
    }

    public enum Pet {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    public enum Owner {
        ADD,
        EDIT,
        DELETE
    }

    public enum Service {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    public enum Appointment {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    public enum Boarding {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    public enum Employee {
        ADD,
        EDIT,
        DELETE,
        RESET
    }

    public enum Payment {
        PAYMENTA,
        PAYMENTB,
        EDITA,
        EDITB,
        PRINTA,
        PRINTB,
        REFUND
    }

    public enum Report {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    // Current active states
    private Page currentPage;
    private Pet currentPetPage;
    private Owner currentOwnerPage;
    private Service currentServicePage;
    private Appointment currentAppointmentPage;
    private Boarding currentBoardingPage;
    private Employee currentEmployeePage;
    private Payment currentPaymentPage;
    private Report currentReportPage;

    // Private constructor to prevent instantiation
    private AppState() {
        currentPage = Page.DASHBOARD; // Default to dashboard
        currentPetPage = Pet.ADD;
        currentOwnerPage = Owner.ADD;
        currentServicePage = Service.ADD;
        currentAppointmentPage = Appointment.ADD;
        currentBoardingPage = Boarding.ADD;
        currentEmployeePage = Employee.ADD;
        currentPaymentPage = Payment.PAYMENTA;
        currentReportPage = Report.ADD;
    }

    // Getter for current page
    public Page getCurrentPage() {
        return currentPage;
    }
    public Pet getCurrentPetPage() {
        return currentPetPage;
    }
    public Owner getCurrentOwnerPage() {
        return currentOwnerPage;
    }
    public Service getCurrentServicePage() { return currentServicePage; }
    public Appointment getCurrentAppointmentPage() { return currentAppointmentPage; }
    public Boarding getCurrentBoardingPage() { return currentBoardingPage; }
    public Employee getCurrentEmployeePage() { return currentEmployeePage; }
    public Payment getCurrentPaymentPage() { return currentPaymentPage; }
    public Report getCurrentReportPage() { return currentReportPage; }
    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    // Setter for current page
    public void setCurrentPage(Page page) {
        currentPage = page;
    }
    public void setCurrentPetPage(Pet page) {
        currentPetPage = page;
    }
    public void setCurrentOwnerPage(Owner page) {
        currentOwnerPage = page;
    }
    public void setCurrentServicePage(Service page) { currentServicePage = page; }
    public void setCurrentAppointmentPage(Appointment page) { currentAppointmentPage = page; }
    public void setCurrentBoardingPage(Boarding page) { currentBoardingPage = page; }
    public void setCurrentEmployeePage(Employee page) { currentEmployeePage = page; }
    public void setCurrentPaymentPage(Payment page) { currentPaymentPage = page; }
    public void setCurrentReportPage(Report page) { currentReportPage = page; }
    public void setCurrentTabIndex(int index) {
        this.currentTabIndex = index;
    }
}

