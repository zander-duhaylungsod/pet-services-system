package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {
    @FXML
    private Button addAppointmentBtn;
    @FXML
    private Button addBoardingBtn;
    @FXML
    private Button addPetBtn;
    @FXML
    private TableColumn<?, ?> appointmentDateColumn;
    @FXML
    private TableColumn<?, ?> appointmentService;
    @FXML
    private TableColumn<?, ?> boardingStartDateColumn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private TableColumn<?, ?> ownerNameColumnA;
    @FXML
    private TableColumn<?, ?> ownerNameColumnB;
    @FXML
    private Button petCounter;
    @FXML
    private TableColumn<?, ?> petNameColumnA;
    @FXML
    private TableColumn<?, ?> petNameColumnB;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Appointment> upcomingAppointmentTable;
    @FXML
    private TableView<Boarding> upcomingBoardingTable;
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<Boarding> boardingList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        AppState.getInstance().setCurrentPage(AppState.Page.DASHBOARD);

        // Set columns for appointments
        ownerNameColumnA.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        petNameColumnA.setCellValueFactory(new PropertyValueFactory<>("petName"));
        appointmentService.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Set columns for boarding reservations
        ownerNameColumnB.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        petNameColumnB.setCellValueFactory(new PropertyValueFactory<>("petName"));
        boardingStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        // Load data
        loadUpcomingAppointments();
        loadUpcomingBoardingReservations();

        // Bind data to tables
        upcomingAppointmentTable.setItems(appointmentList);
        upcomingBoardingTable.setItems(boardingList);
    }

    //database functions
    private Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = ""; // Replace with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }

    private void loadUpcomingAppointments() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT o.FirstName AS ownerName, p.Name AS petName, s.ServiceName, a.Date " +
                             "FROM Appointments a " +
                             "JOIN Pets p ON a.PetID = p.PetID " +
                             "JOIN Owners o ON p.OwnerID = o.OwnerID " +
                             "JOIN Services s ON a.ServiceID = s.ServiceID " +
                             "WHERE a.Date >= CURDATE()")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointmentList.add(new Appointment(
                        rs.getString("ownerName"),
                        rs.getString("petName"),
                        rs.getString("ServiceName"),
                        rs.getString("Date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUpcomingBoardingReservations() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT o.FirstName AS ownerName, p.Name AS petName, b.StartDate " +
                             "FROM BoardingReservations b " +
                             "JOIN Pets p ON b.PetID = p.PetID " +
                             "JOIN Owners o ON p.OwnerID = o.OwnerID " +
                             "WHERE b.StartDate >= CURDATE()")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                boardingList.add(new Boarding(
                        rs.getString("ownerName"),
                        rs.getString("petName"),
                        rs.getString("StartDate")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //scene transitions
    @FXML
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

    @FXML
    public void addPet () throws IOException {
        Main.switchSceneWithFade("scenes/addPet");
    }

    @FXML
    public void addAppointment () throws IOException {
        Main.switchSceneWithFade("scenes/addAppointment");
    }

    @FXML
    public void addBoarding () throws IOException {
        Main.switchSceneWithFade("scenes/addBoarding");
    }

    @FXML
    public void searchFunction(KeyEvent event) {
        //add search function here
    }
}

