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

    // Current active page
    private Page currentPage;

    // Private constructor to prevent instantiation
    private AppState() {
        currentPage = Page.DASHBOARD; // Default to dashboard
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
}

