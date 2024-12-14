package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Label employeeIDLabel;
    @FXML
    private Label employeeNameLabel;
    @FXML
    private Label employeeRoleLabel;
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

    //Observable Lists
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<Boarding> boardingList = FXCollections.observableArrayList();

    //logger
    private static final Logger LOGGER = Logger.getLogger(BoardingController.class.getName());

    @FXML
    public void initialize() {
        AppState.getInstance().setCurrentPage(AppState.Page.DASHBOARD);
        petCounter.setText(PetRecord.getInstance().getPetCount());
        employeeNameLabel.setText(User.getEmployeeName());
        employeeIDLabel.setText(String.valueOf(User.getEmployeeID()));
        employeeRoleLabel.setText(User.getRole());

        // Set columns for upcoming appointments
        ownerNameColumnA.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        petNameColumnA.setCellValueFactory(new PropertyValueFactory<>("petName"));
        appointmentService.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Set columns for upcoming boarding reservations
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
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
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
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    //scene transitions
    @FXML
    public void switchToDashboard () throws IOException {
        NavigationController.switchToDashboard();
    }

    @FXML
    public void switchToRecords () throws IOException {
        NavigationController.switchToRecords();

    }

    @FXML
    public void switchToReports () throws IOException {
        NavigationController.switchToReports();

    }

    @FXML
    public void addPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.ADD);
        Main.switchSceneWithFade("scenes/addPet");
    }

    @FXML
    public void addAppointment () throws IOException {
        AppState.getInstance().setCurrentAppointmentPage(AppState.Appointment.ADD);
        Main.switchSceneWithFade("scenes/addAppointment");
    }

    @FXML
    public void addBoarding () throws IOException {
        AppState.getInstance().setCurrentBoardingPage(AppState.Boarding.ADD);
        Main.switchSceneWithFade("scenes/addBoarding");
    }

    @FXML
    public void logOut () throws IOException {
        NavigationController.logOut();
    }

    @FXML
    public void searchFunction(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();

        // Clear existing filtered lists
        FilteredList<Appointment> filteredAppointments = new FilteredList<>(appointmentList, p -> true);
        FilteredList<Boarding> filteredBoardings = new FilteredList<>(boardingList, p -> true);

        filteredAppointments.setPredicate(appointment -> {
            // If search text is empty, show all items
            if (searchText.isEmpty()) {
                return true;
            }

            return appointment.getOwnerName().toLowerCase().contains(searchText) ||
                    appointment.getPetName().toLowerCase().contains(searchText) ||
                    appointment.getServiceName().toLowerCase().contains(searchText) ||
                    appointment.getAppointmentDate().toLowerCase().contains(searchText);
        });

        filteredBoardings.setPredicate(boarding -> {
            // If search text is empty, show all items
            if (searchText.isEmpty()) {
                return true;
            }

            return boarding.getOwnerName().toLowerCase().contains(searchText) ||
                    boarding.getPetName().toLowerCase().contains(searchText) ||
                    boarding.getStartDate().toLowerCase().contains(searchText);
        });

        // Create sorted lists to maintain order
        SortedList<Appointment> sortedAppointments = new SortedList<>(filteredAppointments);
        SortedList<Boarding> sortedBoardings = new SortedList<>(filteredBoardings);

        upcomingAppointmentTable.setItems(sortedAppointments);
        upcomingBoardingTable.setItems(sortedBoardings);

        sortedAppointments.comparatorProperty().bind(upcomingAppointmentTable.comparatorProperty());
        sortedBoardings.comparatorProperty().bind(upcomingBoardingTable.comparatorProperty());
    }
}

