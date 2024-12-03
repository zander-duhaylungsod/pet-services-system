package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Paths;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class PetController {

    @FXML
    private Polygon backBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Spinner<Integer> petAge;
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

    private String petImagePath = null; // To store the image path

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> ageFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        petAge.setValueFactory(ageFactory);
    }

    //database functions
    @FXML
    public void addPetToDatabase () throws IOException {
        try {
            // Validate inputs
            validatePetInputs();

            String name = petName.getText().trim();
            String species = petSpecies.getText().trim();
            String breed = petBreed.getText().trim();
            Integer age = (Integer) petAge.getValue();
            String ownerID = petOwnerID.getText().trim();

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            // Insert data into Pets table
            String query = "INSERT INTO Pets (Name, Species, Breed, Age, OwnerID, PetImagePath) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, species);
            statement.setString(3, breed.isEmpty() ? null : breed);
            statement.setInt(4, age != null ? age : 0);
            statement.setInt(5, Integer.parseInt(ownerID));
            statement.setString(6, petImagePath);

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                System.out.println("Pet added successfully!");
            } else {
                System.out.println("Failed to add pet.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding pet to the database: " + e.getMessage());
        }
    }

    private void validatePetInputs() {
        String name = petName.getText().trim();
        String species = petSpecies.getText().trim();
        String ownerID = petOwnerID.getText().trim();

        if (name.isEmpty()) {
            showAlert("Validation Error", "Pet name is required.");
            return;
        }

        if (species.isEmpty()) {
            showAlert("Validation Error", "Species is required.");
            return;
        }

        if (ownerID.isEmpty()) {
            showAlert("Validation Error", "Owner ID is required.");
            return;
        }

        try {
            Integer.parseInt(ownerID); // Ensure Owner ID is a valid integer
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Owner ID must be a valid number.");
            return;
        }

        if (petImagePath == null || petImagePath.isEmpty()) {
            showAlert("Validation Error", "Please select an image for the pet.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void selectFile() throws IOException {
        FileChooser fileChooser = new FileChooser(); // Set the initial directory to the parent folder of your relative path
        File initialDirectory = new File("src/main/resources/CCE104/images");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Select Pet Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Define the base directory, e.g., the project directory or any directory you want to use
            String baseDirectory = "C:/Users/Admin/IdeaProjects/pet-services-system/src/main/resources/CCE104"; // Replace with your base directory

            // Convert the base directory and the selected file to Path objects
            java.nio.file.Path basePath = Paths.get(baseDirectory).toAbsolutePath();
            java.nio.file.Path filePath = selectedFile.toPath().toAbsolutePath();

            // Manually calculate the relative path
            java.nio.file.Path parentPath = filePath.getParent();
            String relativePath = parentPath != null ? parentPath.toString().replace(basePath.toString(), "") : "";

            // If there's no parentPath, the file is at the root level, use the file name
            if (relativePath.isEmpty()) {
                relativePath = selectedFile.getName();
            }

            // Ensure that the path uses forward slashes
            relativePath = relativePath.replace("\\", "/");

            // Format the path as @../images/paw.png
            String formattedPath = "/" + relativePath + "/" + selectedFile.getName();

            // Store the formatted path for reference
            petImagePath = formattedPath;

            // Create a Circle to be used as a clip
            Circle clip = new Circle(75, 75, 75);
            petImage.setClip(clip);

            // Update the ImageView using the relative path (pass the formatted path directly)
            petImage.setImage(new Image("file:" + formattedPath)); // Load image using relative path
        }
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

