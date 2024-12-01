package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class PetController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Spinner<?> petAge;
    @FXML
    private TextField petBreed;
    @FXML
    private ImageView petImage;
    @FXML
    private Button petImageFile;
    @FXML
    private TextField petName;
    @FXML
    private TextArea petNotes;
    @FXML
    private TextField petOwnerID;
    @FXML
    private TextField petSpecies;
    @FXML
    private Button recordsBtn;
    @FXML
    private Button reportsBtn;

    @FXML
    public void addPetToDatabase () throws IOException {
        //add function here
    }

    @FXML
    public void selectFile () throws IOException {
        //add function here
    }

    @FXML
    public void savePetChanges () throws IOException {
        //add function here
    }

    @FXML
    public void printPet () throws IOException {
        //add function here
    }

    @FXML
    public void addOwner () throws IOException {
        Main.switchSceneWithFade("scenes/addOwner");
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

    //effects
    @FXML
    public void polygonHover () throws IOException {
        backBtn.setFill(Color.web("#48d1dd"));
        backBtn.setCursor(Cursor.HAND);
    }

    public void polygonHoverExited () throws IOException {
        backBtn.setFill(Color.web("#A1DBDD"));
        backBtn.setCursor(Cursor.DEFAULT);
    }
}

