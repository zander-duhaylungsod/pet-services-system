package CCE104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

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
    private TableView<?> BoardingTable;
    @FXML
    private TableView<?> EmployeeTable;
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
    private TableColumn<?, ?> recordsAppointmentDateColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentOwnerIDColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentOwnerNameColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentPetIDColumn;
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

    private ObservableList<PetRecord> petList = FXCollections.observableArrayList();
    private ObservableList<OwnerRecord> ownerList = FXCollections.observableArrayList();
    private ObservableList<ServiceRecord> serviceList = FXCollections.observableArrayList();
    private ObservableList<AppointmentRecord> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        PetRecord.getInstance().setRecordsController(this);
        OwnerRecord.getInstance().setRecordsController(this);
        ServiceRecord.getInstance().setRecordsController(this);
        AppointmentRecord.getInstance().setRecordsController(this);

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
        recordsAppointmentPetIDColumn.setCellValueFactory(new PropertyValueFactory<>("petID"));
        recordsAppointmentPetNameColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        recordsAppointmentOwnerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ownerID"));
        recordsAppointmentOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        recordsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        recordsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        //load Data
        loadPets();
        loadOwners();
        loadServices();

        //bind data to table
        PetTable.setItems(petList);
        OwnerTable.setItems(ownerList);
        ServiceTable.setItems(serviceList);

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
        Main.switchSceneWithFade("scenes/addAppointment");
    }

    @FXML
    public void addBoarding () throws IOException {
        Main.switchSceneWithFade("scenes/addBoarding");
    }

    @FXML
    public void addEmployee () throws IOException {
        Main.switchSceneWithFade("scenes/addEmployee");
    }

    //edit functions
    @FXML
    public void editPet () throws IOException {
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        if (selectedPet == null) {
            showAlert("No Selection", "Please select a pet to edit.");
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
            showAlert("No Selection", "Please select an owner to edit.");
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
            showAlert("No Selection", "Please select a service to edit.");
            return;
        }

        ServiceRecord.setSelectedService(selectedService);
        AppState.getInstance().setCurrentServicePage(AppState.Service.EDIT);
        Main.switchSceneWithFade("scenes/editService");
    }

    @FXML
    public void editAppointment () throws IOException {
        Main.switchSceneWithFade("scenes/editAppointment");
    }

    @FXML
    public void editBoarding () throws IOException {
        Main.switchSceneWithFade("scenes/editBoarding");
    }

    @FXML
    public void editEmployee () throws IOException {
        Main.switchSceneWithFade("scenes/editEmployee");
    }

    //view functions
    @FXML
    public void viewPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.VIEW);
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        if (selectedPet == null) {
            showAlert("No Selection", "Please select a pet to view.");
            return;
        }

        PetRecord.setSelectedPet(selectedPet);
        Main.switchSceneWithFade("scenes/viewPet");
    }

    @FXML
    public void viewService () throws IOException {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            showAlert("No Selection", "Please select a service to view.");
            return;
        }

        ServiceRecord.setSelectedService(selectedService);
        AppState.getInstance().setCurrentServicePage(AppState.Service.VIEW);
        Main.switchSceneWithFade("scenes/viewService");
    }

    @FXML
    public void viewAppointment () throws IOException {
        Main.switchSceneWithFade("scenes/printAppointment");
    }

    @FXML
    public void viewBoarding () throws IOException {
        Main.switchSceneWithFade("scenes/printBoarding");
    }

    //delete functions
    @FXML
    public void deletePet () throws IOException {
        // Get the selected pet
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();

        // Check if a pet is selected
        if (selectedPet == null) {
            showAlert("No Selection", "Please select a pet to delete.");
            return;
        }

        // Confirm deletion with the user
        Boolean confirm = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected pet? \nThis will delete associated records.");

        // Delete the pet from the database
        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Pets WHERE PetID = ?")) {

                stmt.setInt(1, selectedPet.getPetID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Remove the pet from the list and refresh the table
                    petList.remove(selectedPet);
                    PetTable.refresh();
                    showAlert("Deletion Successful", "The pet has been successfully deleted.");
                } else {
                    showAlert("Deletion Failed", "Failed to delete the pet. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Error", "An error occurred while deleting the pet.");
            }
        }
    }

    @FXML
    public void deleteOwner () throws IOException {
        OwnerRecord selectedOwner = OwnerTable.getSelectionModel().getSelectedItem();

        if (selectedOwner == null) {
            showAlert("No Selection", "Please select an owner to delete.");
            return;
        }

        // Confirm deletion with the user
        Boolean confirm = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected owner? \nThis will delete associated records.");

        // Delete the pet from the database
        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Owners WHERE OwnerID = ?")) {

                stmt.setInt(1, selectedOwner.getOwnerID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    ownerList.remove(selectedOwner);
                    OwnerTable.refresh();
                    showAlert("Deletion Successful", "The owner has been successfully deleted.");
                } else {
                    showAlert("Deletion Failed", "Failed to delete the owner. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Error", "An error occurred while deleting the owner.");
            }
        }
    }

    @FXML
    public void deleteService () throws IOException {
        ServiceRecord selectedService = ServiceTable.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            showAlert("No Selection", "Please select a service to delete.");
            return;
        }

        // Confirm deletion with the user
        Boolean confirm = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete the selected service? \nThis will delete associated records.");

        // Delete the pet from the database
        if(confirm) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID = ?")) {

                stmt.setInt(1, selectedService.getServiceID());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    serviceList.remove(selectedService);
                    ServiceTable.refresh();
                    showAlert("Deletion Successful", "The service has been successfully deleted.");
                } else {
                    showAlert("Deletion Failed", "Failed to delete the service. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Error", "An error occurred while deleting the service.");
            }
        }
    }

    @FXML
    public void deleteAppointment () throws IOException {
        //add delete appointment function here
    }

    @FXML
    public void deleteBoarding () throws IOException {
        //add delete boarding function here
    }

    @FXML
    public void deleteEmployee () throws IOException {
        //add delete employee function here
    }

    @FXML
    void searchFunction(KeyEvent event) {
        //add search function here
    }

    //table dependencies & database functions
    private Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = ""; // Replace with your MySQL password
        return DriverManager.getConnection(url, user, password);
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

    //Pet Table Management --
    public void loadPets() {
        petList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "Pets.PetID, Pets.Name, Pets.Species, Pets.Breed, Pets.Age, " +
                             "Pets.OwnerID, Owners.FirstName AS OwnerName, Pets.PetNotes " +
                             ", Pets.PetImagePath " +
                             "FROM Pets " +
                             "LEFT JOIN Owners " +
                             "ON Pets.OwnerID = Owners.OwnerID")) {

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    //transitions and effects
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

    //alerts
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Return true if the user clicked "OK", false otherwise (including "Cancel" or closing the dialog)
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

