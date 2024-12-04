package CCE104;

public class AppState {

    // Singleton instance
    private static AppState instance;

    // Enum to represent the possible pages
    public enum Page {
        DASHBOARD,
        RECORDS,
        REPORTS
    }

    //Enum to represent Pet Buttons
    public enum Pet {
        ADD,
        EDIT,
        VIEW,
        DELETE
    }

    // Current active states
    private Page currentPage;
    private Pet currentPetPage;

    // Private constructor to prevent instantiation
    private AppState() {
        currentPage = Page.DASHBOARD; // Default to dashboard
        currentPetPage = Pet.ADD;
    }

    // Method to get the instance of the AppState
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Getter for current page
    public Page getCurrentPage() {
        return currentPage;
    }

    // Setter for current page
    public void setCurrentPage(Page page) {
        currentPage = page;
    }

    // Getter for current page
    public Pet getCurrentPetPage() {
        return currentPetPage;
    }

    // Setter for current page
    public void setCurrentPetPage(Pet page) {
        currentPetPage = page;
    }
}

