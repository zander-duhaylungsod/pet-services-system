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

public class RecordsController {

    @FXML
    private TableView<PetRecord> PetTable;
    @FXML
    private TableView<?> OwnerTable;
    @FXML
    private TableView<?> ServiceTable;
    @FXML
    private TableView<?> AppointmentTable;
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
    private TableColumn<?, ?> recordsAppointmentOwnerColumn;
    @FXML
    private TableColumn<?, ?> recordsAppointmentPetColumn;
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
    private ObservableList<PetRecord> petList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        PetRecord.getInstance().setRecordsController(this);
        AppState.getInstance().setCurrentPage(AppState.Page.RECORDS);

        // Initialize pet table columns
        recordsPetIDColumn.setCellValueFactory(new PropertyValueFactory<>("petID"));
        recordsPetNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        recordsPetSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        recordsPetBreedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        recordsPetOwnerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ownerID"));
        recordsPetOwnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        //load Data
        loadPets();

        //bind data to table
        PetTable.setItems(petList);
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

    //add functions
    @FXML
    public void addPet () throws IOException {
        Main.switchSceneWithFade("scenes/addPet");
    }

    @FXML
    public void addOwner () throws IOException {
        Main.switchSceneWithFade("scenes/addOwner");
    }

    @FXML
    public void addService () throws IOException {
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
        Main.switchSceneWithFade("scenes/editPet");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void editOwner () throws IOException {
        Main.switchSceneWithFade("scenes/editOwner");
    }

    @FXML
    public void editService () throws IOException {
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
        Main.switchSceneWithFade("scenes/viewPet");
    }

    @FXML
    public void viewService () throws IOException {
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
        //add delete pet function here
    }

    @FXML
    public void deleteOwner () throws IOException {
        //add delete owner function here
    }

    @FXML
    public void deleteService () throws IOException {
        //add delete service function here
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

    public Integer getSelectedPetID() {
        PetRecord selectedPet = PetTable.getSelectionModel().getSelectedItem();
        return selectedPet != null ? selectedPet.getPetID() : null;
    }

    //Pet Table Management --
    public void loadPets() {
        petList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " +
                             "Pets.PetID, Pets.Name, Pets.Species, Pets.Breed, Pets.Age, " +
                             "Pets.OwnerID, Owners.FirstName AS OwnerName, Pets.PetImagePath " +
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
                        rs.getString("PetImagePath")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

