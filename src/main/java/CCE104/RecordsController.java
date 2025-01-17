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

public class RecordsController {

    @FXML
    private TableView<PetRecord> PetTable;
    @FXML
    private TableView<OwnerRecord> OwnerTable;
    @FXML
    private TableView<ServiceRecord> ServiceTable;
    @FXML
    private TableView<AppointmentRecord> AppointmentTable;
    @FXML
    private TableView<BoardingRecord> BoardingTable;
    @FXML
    private TableView<EmployeeRecord> EmployeeTable;
    @FXML
    private Tab appointmentTab;
    @FXML
    private Tab boardingTab;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button deleteOwner;
    @FXML
    private Tab employeeTab;
    @FXML
    private Button logOutBtn;
    @FXML
    private Tab ownerTab;
    @FXML
    private Tab petTab;
    @FXML
    private TableColumn<?, ?> recordsAppointmentIDColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentDateColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentTimeColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentOwnerNameColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentPetNameColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentServiceColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentStatusColumn;
    @FXML
    private TableColumn<?, ?> recordsBoardingCheckOutDate;
    @FXML
    private TableColumn<?, ?> recordsBoardingCheckinDate;
    @FXML
    private TableColumn<?, ?> recordsBoardingOwnerColumn;
    @FXML
    private TableColumn<?, ?> recordsBoardingIDColumn;
    @FXML
    private TableColumn<?, ?> recordsBoardingPetColumn;
    @FXML
    private TableColumn<?, ?> recordsBoardingStatusColumn;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button petAddBtn;
    @FXML
    private Button petDeleteBtn;
    @FXML
    private Button petEditBtn;
    @FXML
    private Button petCounter;
    @FXML
    private TableColumn<?, ?> recordsDescriptionColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeeBranchColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeeFirstNameColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeeIDColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeeLastNameColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeePhoneColumn;
    @FXML
    private TableColumn<?, ?> recordsEmployeeRoleColumn;
    @FXML
    private TableColumn<?, ?> recordsOwnerEmailColumn;
    @FXML
    private TableColumn<?, ?> recordsOwnerFirstNameColumn;
    @FXML
    private TableColumn<?, ?> recordsOwnerIDColumn;
    @FXML
    private TableColumn<?, ?> recordsOwnerLastName;
    @FXML
    private TableColumn<?, ?> recordsOwnerPhoneColumn;
    @FXML
    private TableColumn<?, ?> recordsPetBreedColumn;
    @FXML
    private TableColumn<?, ?> recordsPetIDColumn;
    @FXML
    private TableColumn<?, ?> recordsPetNameColumn;
    @FXML
    private TableColumn<?, ?> recordsPetOwnerIDColumn;
    @FXML
    private TableColumn<?, ?> recordsPetOwnerNameColumn;
    @FXML
    private TableColumn<?, ?> recordsPetSpeciesColumn;
    @FXML
    private TableColumn<?, ?> recordsPriceColumn;
    @FXML
    private TableColumn<?, ?> recordsServicesColumn;
    @FXML
    private Button reportsBtn;
    @FXML
    private Tab serviceTab;
    @FXML
    private TabPane recordsTabPane;
    @FXML
    private TextField searchField;
    @FXML
    private Label employeeIDLabel;
    @FXML
    private Label employeeNameLabel;
    @FXML
    private Label employeeRoleLabel;

    private final ObservableList<PetRecord> petList = FXCollections.observableArrayList();
    private final ObservableList<OwnerRecord> ownerList = FXCollections.observableArrayList();
    private final ObservableList<ServiceRecord> serviceList = FXCollections.observableArrayList();
    private final ObservableList<AppointmentRecord> appointmentList = FXCollections.observableArrayList();
    private final ObservableList<BoardingRecord> boardingList = FXCollections.observableArrayList();
    private final ObservableList<EmployeeRecord> employeeList = FXCollections.observableArrayList();

    //Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //logger
    private static final Logger LOGGER = Logger.getLogger(RecordsController.class.getName());

    @FXML
    public void initialize() {
        petCounter.setText(PetRecord.getInstance().getPetCount());
        employeeNameLabel.setText(User.getEmployeeName());
        employeeIDLabel.setText(String.valueOf(User.getEmployeeID()));
        employeeRoleLabel.setText(User.getRole());

        PetRecord.getInstance().setRecordsController(this);
        OwnerRecord.getInstance().setRecordsController(this);
        ServiceRecord.getInstance().setRecordsController(this);
        AppointmentRecord.getInstance().setRecordsController(this);
        BoardingRecord.getInstance().setRecordsController(this);
        EmployeeRecord.getInstance().setRecordsController(this);

        AppState.getInstance().setCurrentPage(AppState.Page.RECORDS);

        // Initialize pets table columns
        recordsPetIDColumn.setCellValueFactory(new PropertyValueFactory<>("petID"));
        recordsPetNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        recordsPetSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        recordsPetBreedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        recordsPetOwnerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ownerID"));
        recordsPetOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        // Initialize owners table columns
        recordsOwnerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        recordsOwnerFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        recordsOwnerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ownerID"));
        recordsOwnerLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        recordsOwnerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Initialize services table columns
        recordsServicesColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        recordsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        recordsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Initialize appointments table columns
        recordsAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        recordsAppointmentPetNameColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        recordsAppointmentOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        recordsAppointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        recordsAppointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        recordsAppointmentServiceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        recordsAppointmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize boarding table columns
        recordsBoardingIDColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        recordsBoardingPetColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        recordsBoardingOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        recordsBoardingCheckinDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        recordsBoardingCheckOutDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        recordsBoardingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize employee table columns
        recordsEmployeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        recordsEmployeeFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        recordsEmployeeLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        recordsEmployeePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        recordsEmployeeRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        //load Data
        loadPets();
        loadOwners();
        loadServices();
        loadAppointments();
        loadBoarding();
        loadEmployees();

        //bind data to table
        PetTable.setItems(petList);
        OwnerTable.setItems(ownerList);
        ServiceTable.setItems(serviceList);
        AppointmentTable.setItems(appointmentList);
        BoardingTable.setItems(boardingList);
        EmployeeTable.setItems(employeeList);


        // Tabpane Setup
        recordsTabPane.getSelectionModel().select(AppState.getInstance().getCurrentTabIndex());
        recordsTabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            AppState.getInstance().setCurrentTabIndex(newValue.intValue());
        });
    }

    //add functions
    @FXML
    public void addPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.ADD);
        Main.switchSceneWithFade("scenes/addPet");
    }

    @FXML
    public void addOwner () throws IOException {
        AppState.getInstance().setCurrentOwnerPage(AppState.Owner.ADD);
        Main.switchSceneWithFade("scenes/addOwner");
    }

    @FXML
    public void addService () throws IOException {
        AppState.getInstance().setCurrentServicePage(AppState.Service.ADD);
        Main.switchSceneWithFade("scenes/addService");
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
    public void addEmployee () throws IOException {
        AppState.getInstance().setCurrentEmployeePage(AppState.Employee.ADD);
        Main.switchSceneWithFade("scenes/addEmployee");
    }

    //edit functions
    @FXML
    public void editPet () throws IOException {
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        if (selectedPet == null) {
            Alerts.showAlert("No Selection", "Please select a pet to edit.");
            return;
        }

        PetRecord.setSelectedPet(selectedPet);
        AppState.getInstance().setCurrentPetPage(AppState.Pet.EDIT);
        Main.switchSceneWithFade("scenes/editPet");
    }

    @FXML
    public void editOwner () throws IOException {
        OwnerRecord selectedOwner = OwnerTable.getSelectionModel().getSelectedItem();

        if (selectedOwner == null) {
            Alerts.showAlert("No Selection", "Please select an owner to edit.");
            return;
        }

        OwnerRecord.setSelectedOwner(selectedOwner);
        AppState.getInstance().setCurrentOwnerPage(AppState.Owner.EDIT);
        Main.switchSceneWithFade("scenes/editOwner");
    }

    @FXML
    public void editService () throws IOException {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            Alerts.showAlert("No Selection", "Please select a service to edit.");
            return;
        }

        ServiceRecord.setSelectedService(selectedService);
        AppState.getInstance().setCurrentServicePage(AppState.Service.EDIT);
        Main.switchSceneWithFade("scenes/editService");
    }

    @FXML
    public void editAppointment () throws IOException {
        AppointmentRecord selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alerts.showAlert("No Selection", "Please select an appointment to update.");
            return;
        }

        AppointmentRecord.setSelectedAppointment(selectedAppointment);
        AppState.getInstance().setCurrentAppointmentPage(AppState.Appointment.EDIT);
        Main.switchSceneWithFade("scenes/editAppointment");
    }

    @FXML
    public void editBoarding () throws IOException {
        BoardingRecord selectedBoarding = BoardingTable.getSelectionModel().getSelectedItem();

        if (selectedBoarding == null) {
            Alerts.showAlert("No Selection", "Please select a reservation to update.");
            return;
        }

        BoardingRecord.setSelectedBoarding(selectedBoarding);
        AppState.getInstance().setCurrentBoardingPage(AppState.Boarding.EDIT);
        Main.switchSceneWithFade("scenes/editBoarding");
    }

    @FXML
    public void editEmployee () throws IOException {
        EmployeeRecord selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            Alerts.showAlert("No Selection", "Please select an employee to edit.");
            return;
        }

        EmployeeRecord.setSelectedEmployee(selectedEmployee);
        AppState.getInstance().setCurrentEmployeePage(AppState.Employee.EDIT);
        Main.switchSceneWithFade("scenes/editEmployee");
    }

    //view functions
    @FXML
    public void viewPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.VIEW);
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        if (selectedPet == null) {
            Alerts.showAlert("No Selection", "Please select a pet to view.");
            return;
        }

        PetRecord.setSelectedPet(selectedPet);
        Main.switchSceneWithFade("scenes/viewPet");
    }

    @FXML
    public void viewService () throws IOException {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            Alerts.showAlert("No Selection", "Please select a service to view.");
            return;
        }

        ServiceRecord.setSelectedService(selectedService);
        AppState.getInstance().setCurrentServicePage(AppState.Service.VIEW);
        Main.switchSceneWithFade("scenes/viewService");
    }

    @FXML
    public void viewAppointment () throws IOException {
        AppointmentRecord selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alerts.showAlert("No Selection", "Please select an appointment to update.");
            return;
        }

        AppointmentRecord.setSelectedAppointment(selectedAppointment);
        AppState.getInstance().setCurrentAppointmentPage(AppState.Appointment.VIEW);
        Main.switchSceneWithFade("scenes/printAppointment");
    }

    @FXML
    public void viewBoarding () throws IOException {
        BoardingRecord selectedBoarding = BoardingTable.getSelectionModel().getSelectedItem();

        if (selectedBoarding == null) {
            Alerts.showAlert("No Selection", "Please select a reservation to view.");
            return;
        }

        BoardingRecord.setSelectedBoarding(selectedBoarding);
        AppState.getInstance().setCurrentBoardingPage(AppState.Boarding.VIEW);
        Main.switchSceneWithFade("scenes/printBoarding");
    }

    //delete functions
    @FXML
    public void deletePet () throws IOException {
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        if (selectedPet == null) {
            Alerts.showAlert("No Selection", "Please select a pet to delete.");
            return;
        }

        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected pet?\nThis will delete associated records.");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Pets WHERE PetID = ?")) {

                stmt.setInt(1, selectedPet.getPetID());

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    petList.remove(selectedPet);
                    PetTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The pet record has been successfully deleted.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete the pet record. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Pet Deletion Error","An error occurred while deleting the pet record.");
            }
        }
    }

    @FXML
    public void deleteOwner () throws IOException {
        OwnerRecord selectedOwner = OwnerTable.getSelectionModel().getSelectedItem();

        if (selectedOwner == null) {
            Alerts.showAlert("No Selection", "Please select an owner to delete.");
            return;
        }

        // Confirm deletion with the user
        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected owner?\nThis will delete associated records.");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Owners WHERE OwnerID = ?")) {

                stmt.setInt(1, selectedOwner.getOwnerID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    ownerList.remove(selectedOwner);
                    OwnerTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The owner record has been successfully deleted.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete the owner record. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Owner Deletion Error","An error occurred while deleting the owner record.");
            }
        }
    }

    @FXML
    public void deleteService () throws IOException {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            Alerts.showAlert("No Selection", "Please select a service to delete.");
            return;
        }

        // Confirm deletion with the user
        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected service?\nThis will delete associated records.");

        // Delete the pet from the database
        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID = ?")) {

                stmt.setInt(1, selectedService.getServiceID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    serviceList.remove(selectedService);
                    ServiceTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The service has been successfully deleted.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete the service. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Service Deletion Error","An error occurred while deleting the service.");
            }
        }
    }

    @FXML
    public void deleteAppointment () throws IOException {
        AppointmentRecord selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alerts.showAlert("No Selection", "Please select an appointment to delete.");
            return;
        }

        // Confirm deletion with the user
        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected appointment?\nThis will affect associated records.");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Appointments WHERE AppointmentID = ?")) {

                stmt.setInt(1, selectedAppointment.getAppointmentID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    appointmentList.remove(selectedAppointment);
                    AppointmentTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The appointment has been successfully terminated.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete appointment. Please check and try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Appointment Deletion Error","An error occurred while deleting the appointment.");
            }
        }
    }

    @FXML
    public void deleteBoarding () throws IOException {
        BoardingRecord selectedBoarding = BoardingTable.getSelectionModel().getSelectedItem();

        if (selectedBoarding == null) {
            Alerts.showAlert("No Selection", "Please select a reservation to delete.");
            return;
        }

        // Confirm deletion with the user
        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected reservation?\nThis will affect associated records.");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM BoardingReservations WHERE ReservationID = ?")) {

                stmt.setInt(1, selectedBoarding.getReservationID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    boardingList.remove(selectedBoarding);
                    BoardingTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The reservation has been successfully terminated.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete reservation. Please check and try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Boarding Deletion Error","An error occurred while deleting the reservation.");
            }
        }
    }

    @FXML
    public void deleteEmployee () throws IOException {
        EmployeeRecord selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            Alerts.showAlert("No Selection", "Please select an employee to delete.");
            return;
        }

        boolean confirm = Alerts.showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected employee?\nThis will delete associated records.");

        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Employees WHERE EmployeeID = ?")) {

                stmt.setInt(1, selectedEmployee.getEmployeeID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    employeeList.remove(selectedEmployee);
                    EmployeeTable.refresh();
                    Alerts.showAlert("Deletion Successful", "The employee record has been successfully deleted.");
                } else {
                    Alerts.showAlert("Deletion Failed", "Failed to delete the employee record. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An Exception occurred", e);
                Alerts.showErrorDialog("Employee Record Deletion Error","An error occurred while deleting the employee record.");
            }
        }
    }

    //table dependencies & database functions
    private Connection connect() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //ID getters
    public Integer getSelectedPetID() {
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();
        return selectedPet != null ? selectedPet.getPetID() : null;
    }

    public Integer getSelectedOwnerID() {
        OwnerRecord selectedOwner = OwnerTable.getSelectionModel().getSelectedItem();
        return selectedOwner != null ? selectedOwner.getOwnerID() : null;
    }

    public Integer getSelectedServiceID() {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();
        return selectedService != null ? selectedService.getServiceID() : null;
    }

    public Integer getSelectedAppointmentID() {
        AppointmentRecord selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
        return selectedAppointment != null ? selectedAppointment.getAppointmentID() : null;
    }

    public Integer getSelectedBoardingID() {
        BoardingRecord selectedBoarding = BoardingTable.getSelectionModel().getSelectedItem();
        return selectedBoarding != null ? selectedBoarding.getReservationID() : null;
    }

    public Integer getSelectedEmployeeID() {
        EmployeeRecord selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();
        return selectedEmployee != null ? selectedEmployee.getEmployeeID() : null;
    }

    //Table Loading --
    public void loadPets() {
        petList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "Pets.PetID, Pets.Name, Pets.Species, Pets.Breed, Pets.Age, " +
                             "Pets.OwnerID, CONCAT(Owners.FirstName,' ',Owners.LastName) AS OwnerName, Pets.PetNotes " +
                             ", Pets.PetImagePath " +
                             "FROM Pets " +
                             "LEFT JOIN Owners " +
                             "ON Pets.OwnerID = Owners.OwnerID;")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                petList.add(new PetRecord(
                        rs.getInt("PetID"),
                        rs.getString("Name"),
                        rs.getString("Species"),
                        rs.getString("Breed"),
                        rs.getInt("Age"),
                        rs.getInt("OwnerID"),
                        rs.getString("OwnerName"),
                        rs.getString("PetNotes"),
                        rs.getString("PetImagePath")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public void loadOwners() {
        ownerList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "Owners.OwnerID, Owners.FirstName, Owners.LastName, Owners.Email, Owners.Phone " +
                             "FROM Owners;"
             )) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ownerList.add(new OwnerRecord(
                        rs.getInt("OwnerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public void loadServices() {
        serviceList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "Services.ServiceID, Services.ServiceName, Services.Price, Services.Description " +
                             "FROM Services;")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                serviceList.add(new ServiceRecord(
                        rs.getInt("ServiceID"),
                        rs.getString("ServiceName"),
                        rs.getDouble("Price"),
                        rs.getString("Description")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public void loadAppointments() {
        appointmentList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "a.AppointmentID, " +
                             "a.Date, " +
                             "a.Time, " +
                             "a.ServiceID, " +
                             "s.ServiceName, " +
                             "a.PetID, " +
                             "p.Name, " +
                             "o.FirstName, " +
                             "o.LastName, " +
                             "a.Status " +
                             "FROM Appointments a " +
                             "JOIN Services s ON a.ServiceID = s.ServiceID " +
                             "JOIN Pets p ON a.PetID = p.PetID " +
                             "JOIN Owners o ON p.OwnerID = o.OwnerID;"
             )) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("FirstName") + " " + rs.getString("LastName");
                appointmentList.add(new AppointmentRecord(
                        rs.getInt("AppointmentID"),
                        rs.getString("Name"),
                        fullName,
                        rs.getDate("Date"),
                        rs.getTime("Time"),
                        rs.getString("ServiceName"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public void loadBoarding() {
        boardingList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "b.ReservationID, " +
                             "p.Name AS PetName, " +
                             "o.FirstName, " +
                             "o.LastName, " +
                             "b.StartDate, " +
                             "b.EndDate, " +
                             "b.Status " +
                             "FROM BoardingReservations b " +
                             "JOIN Pets p ON b.PetID = p.PetID " +
                             "JOIN Owners o ON p.OwnerID = o.OwnerID;"
             )) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("FirstName") + " " + rs.getString("LastName");
                boardingList.add(new BoardingRecord(
                        rs.getInt("ReservationID"),
                        rs.getString("PetName"),
                        fullName, // Concatenated first and last name
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    public void loadEmployees() {
        employeeList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT EmployeeID, FirstName, LastName, Phone, Role FROM Employees;"
             )) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employeeList.add(new EmployeeRecord(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Phone"),
                        rs.getString("Role")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    @FXML
    public void searchFunction(KeyEvent event) {
        // Get the search text and convert to lowercase for case-insensitive search
        String searchText = searchField.getText().toLowerCase().trim();

        // Create filtered lists for each type of record
        FilteredList<PetRecord> filteredPets = new FilteredList<>(petList, p -> true);
        FilteredList<OwnerRecord> filteredOwners = new FilteredList<>(ownerList, p -> true);
        FilteredList<ServiceRecord> filteredServices = new FilteredList<>(serviceList, p -> true);
        FilteredList<AppointmentRecord> filteredAppointments = new FilteredList<>(appointmentList, p -> true);
        FilteredList<BoardingRecord> filteredBoarding = new FilteredList<>(boardingList, p -> true);
        FilteredList<EmployeeRecord> filteredEmployees = new FilteredList<>(employeeList, p -> true);

        // Predicates for filtering each type of record
        filteredPets.setPredicate(pet -> {
            if (searchText.isEmpty()) return true;
            return pet.getName().toLowerCase().contains(searchText) ||
                    pet.getSpecies().toLowerCase().contains(searchText) ||
                    pet.getBreed().toLowerCase().contains(searchText) ||
                    pet.getOwnerName().toLowerCase().contains(searchText) ||
                    String.valueOf(pet.getPetAge()).contains(searchText);
        });

        filteredOwners.setPredicate(owner -> {
            if (searchText.isEmpty()) return true;
            return owner.getFirstName().toLowerCase().contains(searchText) ||
                    owner.getLastName().toLowerCase().contains(searchText) ||
                    owner.getEmail().toLowerCase().contains(searchText) ||
                    owner.getPhone().toLowerCase().contains(searchText);
        });

        filteredServices.setPredicate(service -> {
            if (searchText.isEmpty()) return true;
            return service.getServiceName().toLowerCase().contains(searchText) ||
                    service.getDescription().toLowerCase().contains(searchText) ||
                    String.valueOf(service.getPrice()).contains(searchText);
        });

        filteredAppointments.setPredicate(appointment -> {
            if (searchText.isEmpty()) return true;
            return appointment.getPetName().toLowerCase().contains(searchText) ||
                    appointment.getOwnerName().toLowerCase().contains(searchText) ||
                    appointment.getServiceName().toLowerCase().contains(searchText) ||
                    appointment.getDate().toString().contains(searchText) ||
                    appointment.getTime().toString().contains(searchText) ||
                    appointment.getStatus().toLowerCase().contains(searchText);
        });

        filteredBoarding.setPredicate(boarding -> {
            if (searchText.isEmpty()) return true;
            return boarding.getPetName().toLowerCase().contains(searchText) ||
                    boarding.getOwnerName().toLowerCase().contains(searchText) ||
                    boarding.getStartDate().toString().contains(searchText) ||
                    boarding.getEndDate().toString().contains(searchText) ||
                    boarding.getStatus().toLowerCase().contains(searchText);
        });

        filteredEmployees.setPredicate(employee -> {
            if (searchText.isEmpty()) return true;
            return employee.getFirstName().toLowerCase().contains(searchText) ||
                    employee.getLastName().toLowerCase().contains(searchText) ||
                    employee.getPhone().toLowerCase().contains(searchText) ||
                    employee.getRole().toLowerCase().contains(searchText);
        });

        // Create sorted lists to maintain order
        SortedList<PetRecord> sortedPets = new SortedList<>(filteredPets);
        SortedList<OwnerRecord> sortedOwners = new SortedList<>(filteredOwners);
        SortedList<ServiceRecord> sortedServices = new SortedList<>(filteredServices);
        SortedList<AppointmentRecord> sortedAppointments = new SortedList<>(filteredAppointments);
        SortedList<BoardingRecord> sortedBoarding = new SortedList<>(filteredBoarding);
        SortedList<EmployeeRecord> sortedEmployees = new SortedList<>(filteredEmployees);

        // Bind the sorted lists to their respective table views
        PetTable.setItems(sortedPets);
        OwnerTable.setItems(sortedOwners);
        ServiceTable.setItems(sortedServices);
        AppointmentTable.setItems(sortedAppointments);
        BoardingTable.setItems(sortedBoarding);
        EmployeeTable.setItems(sortedEmployees);

        // Bind comparators if you want to maintain table sorting
        sortedPets.comparatorProperty().bind(PetTable.comparatorProperty());
        sortedOwners.comparatorProperty().bind(OwnerTable.comparatorProperty());
        sortedServices.comparatorProperty().bind(ServiceTable.comparatorProperty());
        sortedAppointments.comparatorProperty().bind(AppointmentTable.comparatorProperty());
        sortedBoarding.comparatorProperty().bind(BoardingTable.comparatorProperty());
        sortedEmployees.comparatorProperty().bind(EmployeeTable.comparatorProperty());
    }

    //transitions and effects
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
    public void logOut () throws IOException {
        NavigationController.logOut();
    }

    //alerts
    public void onlyAuthorizedAlert () {
        int currentIndex = AppState.getInstance().getCurrentTabIndex();
        if (currentIndex == 5) {
            Alerts.showAlert("Unauthorized Access", "You are not authorized to access this page.");
        }
    }
}

