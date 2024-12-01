package CCE104;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

public class RecordsController {

    @FXML
    private TableView<?> PetOwnerTable;
    @FXML
    private TableView<?> PetOwnerTable1;
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


    @FXML
    public void initialize() {
        AppState.getInstance().setCurrentPage(AppState.Page.RECORDS);
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
        Main.switchSceneWithFade("scenes/editPet");
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
}

