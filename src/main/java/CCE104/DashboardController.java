package CCE104;

import javafx.fxml.FXML;

import java.io.IOException;

public class DashboardController {
    @FXML
    private NavigationController navigationController;

    @FXML
    public void initialize() throws IOException {
        // Delegating tasks to secondary controllers
        navigationController.switchToDashboard();
        navigationController.switchToRecords();
        navigationController.switchToReports();
        navigationController.logOut();
    }
}
