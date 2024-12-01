package CCE104;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class ReportsPageController {

    @FXML
    private TableColumn<?, ?> amountColumn;
    @FXML
    private TableColumn<?, ?> createdByColumn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private TableColumn<?, ?> dateColumn;
    @FXML
    private Label employeeName;
    @FXML
    private Label employeeRole;
    @FXML
    private Button logOutBtn;
    @FXML
    private TableColumn<?, ?> ownerColumn;
    @FXML
    private TableColumn<?, ?> paymentDateColumn;
    @FXML
    private TableColumn<?, ?> paymentIDColumn;
    @FXML
    private TableColumn<?, ?> paymentStatus;
    @FXML
    private TableColumn<?, ?> petColumn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<?, ?> serviceProvidedColumn;
    @FXML
    private TableColumn<?, ?> titleColumn;
    @FXML
    private TableColumn<?, ?> typeColumn;

    public void initialize() {
        AppState.getInstance().setCurrentPage(AppState.Page.REPORTS);
    }

    @FXML
    public void addPayment() throws IOException{
        Main.switchSceneWithFade("scenes/addPayment");
    }

    @FXML
    void editPayment() throws IOException{
        Main.switchSceneWithFade("scenes/editPayment");
    }

    @FXML
    void printPayment() throws IOException{
        Main.switchSceneWithFade("scenes/printPayment");
    }

    @FXML
    void refundPayment() throws IOException{
        Main.switchSceneWithFade("scenes/refundPayment");
    }

    @FXML
    void addReport() throws IOException{
        Main.switchSceneWithFade("scenes/addReport");
    }

    @FXML
    void editReport() throws IOException{
        Main.switchSceneWithFade("scenes/editReport");
    }

    @FXML
    void viewReport() throws IOException{
        Main.switchSceneWithFade("scenes/viewReport");
    }

    @FXML
    void deleteReport() throws IOException{
        //add function here
    }

    public void backFunction () throws IOException {
        AppState.Page currentPage = AppState.getInstance().getCurrentPage();

        if (currentPage == AppState.Page.DASHBOARD) {
            switchToDashboard();
        } else if (currentPage == AppState.Page.RECORDS) {
            switchToRecords();
        } else if (currentPage == AppState.Page.REPORTS) {
            switchToReports();
        }
    }

    @FXML
    public void searchFunction(KeyEvent event) {
        //add search function here
    }

    public void switchToDashboard () throws IOException {
        Main.switchScene("scenes/dashboardAdmin");
    }

    @FXML
    public void switchToRecords () throws IOException {
        Main.switchScene("scenes/recordsAdmin");
    }

    @FXML
    public void switchToReports () throws IOException {
        Main.switchScene("scenes/reportsAdmin");
    }

    @FXML
    public void logOut () throws IOException {
        Main.switchSceneWithFade("scenes/signIn");
    }

}
