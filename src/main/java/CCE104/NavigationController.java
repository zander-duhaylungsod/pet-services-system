package CCE104;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class NavigationController {

    @FXML
    public static void switchToDashboard () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchScene("scenes/dashboardAdmin");
        } else {
            Main.switchScene("scenes/dashboardEmployee");
        }
    }

    @FXML
    public static void switchToRecords () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchScene("scenes/recordsAdmin");
        } else {
            Main.switchScene("scenes/recordsEmployee");
        }
    }

    @FXML
    public static void switchToReports () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchScene("scenes/reportsAdmin");
        } else {
            Main.switchScene("scenes/reportsEmployee");
        }
    }

    @FXML
    public static void switchToDashboardWithFade () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/dashboardAdmin");
        } else {
            Main.switchSceneWithFade("scenes/dashboardEmployee");
        }
    }

    @FXML
    public static void switchToRecordsWithFade () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/recordsAdmin");
        } else {
            Main.switchSceneWithFade("scenes/recordsEmployee");
        }
    }

    @FXML
    public static void switchToReportsWithFade () throws IOException {
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/reportsAdmin");
        } else {
            Main.switchSceneWithFade("scenes/reportsEmployee");
        }
    }

    @FXML
    public void addPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.ADD);
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/addPet");
        } else {
            Main.switchSceneWithFade("scenes/addPetEmployee");
        }
    }

    @FXML
    public void editPet () throws IOException {
        AppState.getInstance().setCurrentPetPage(AppState.Pet.ADD);
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/editPet");
        } else {
            Main.switchSceneWithFade("scenes/editPetEmployee");
        }
    }

    @FXML
    public void addAppointment () throws IOException {
        AppState.getInstance().setCurrentAppointmentPage(AppState.Appointment.ADD);
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/addAppointment");
        } else {
            Main.switchSceneWithFade("scenes/addAppointmentEmployee");
        }
    }

    @FXML
    public void editAppointment () throws IOException {
        AppState.getInstance().setCurrentAppointmentPage(AppState.Appointment.ADD);
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/editAppointment");
        } else {
            Main.switchSceneWithFade("scenes/editAppointmentEmployee");
        }
    }

    @FXML
    public void addBoarding () throws IOException {
        AppState.getInstance().setCurrentBoardingPage(AppState.Boarding.ADD);
        if(AppState.getInstance().getCurrentLevelPage() == AppState.Level.ADMIN) {
            Main.switchSceneWithFade("scenes/addBoarding");
        } else {
            Main.switchSceneWithFade("scenes/addBoarding");
        }
    }
}
