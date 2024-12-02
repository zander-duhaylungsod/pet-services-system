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
import java.sql.SQLException;

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
        FileChooser fileChooser = new FileChooser();

        // Use project root directory dynamically
        File projectRoot = new File(System.getProperty("user.dir"));
        File initialDirectory = new File(projectRoot, "src/main/resources/CCE104/images");

        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Select Pet Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Get the project root directory dynamically
                java.nio.file.Path projectRootPath = projectRoot.toPath().toAbsolutePath();
                java.nio.file.Path selectedFilePath = selectedFile.toPath().toAbsolutePath();

                // Calculate relative path from the resources directory
                java.nio.file.Path resourcesPath = projectRootPath.resolve("src/main/resources");
                java.nio.file.Path relativePath = resourcesPath.relativize(selectedFilePath.getParent());

                // Construct the formatted path
                String formattedPath = "/" + relativePath.toString().replace("\\", "/") + "/" + selectedFile.getName();

                // Store the formatted path
                petImagePath = formattedPath;

                // Create a Circle to be used as a clip
                Circle clip = new Circle(75, 75, 75);
                petImage.setClip(clip);

                // Update the ImageView using the relative path
                petImage.setImage(new Image("file:" + new File(resourcesPath.toFile(), formattedPath).getAbsolutePath()));
            } catch (Exception e) {
                // Handle any potential path resolution errors
                e.printStackTrace();

                showErrorDialog("Failed to select image", "Could not resolve image path");
            }
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void savePetChanges () throws IOException, SQLException {
        try {
            // Step 1: Validate input fields
            String name = petName.getText();
            String species = petSpecies.getText();
            String breed = petBreed.getText();
            Integer age = petAge.getValue();
            String ownerID = petOwnerID.getText();

            if (name == null || name.isEmpty()) {
                showErrorDialog("Validation Error", "Pet name cannot be empty.");
                return;
            }
            if (species == null || species.isEmpty()) {
                showErrorDialog("Validation Error", "Pet species cannot be empty.");
                return;
            }
            if (breed == null || breed.isEmpty()) {
                showErrorDialog("Validation Error", "Pet breed cannot be empty.");
                return;
            }
            if (age == null || age <= 0) {
                showErrorDialog("Validation Error", "Age must be a positive number.");
                return;
            }
            if (ownerID == null || ownerID.isEmpty() || !ownerID.matches("\\d+")) {
                showErrorDialog("Validation Error", "Owner ID must be a valid number.");
                return;
            }

            // Step 2: Get the selected Pet ID from RecordsController
            RecordsController recordsController = PetRecord.getInstance().getRecordsController();
            Integer selectedPetID = recordsController.getSelectedPetID();
            if (selectedPetID == null) {
                showErrorDialog("No Pet Selected", "Please select a pet to edit.");
                return;
            }

            // Step 3: Update pet in the database
            String updateQuery = "UPDATE Pets SET Name = ?, Species = ?, Breed = ?, Age = ?, OwnerID = ?, PetImagePath = ? WHERE PetID = ?";
            try (Connection conn = DriverManager.getConnection(url,user,password);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, name);
                stmt.setString(2, species);
                stmt.setString(3, breed);
                stmt.setInt(4, age);
                stmt.setInt(5, Integer.parseInt(ownerID));
                stmt.setString(6, petImagePath);
                stmt.setInt(7, selectedPetID);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showSuccessDialog("Success", "Pet details updated successfully.");
                    //recordsController.refreshPetTable(); // Refresh the table in RecordsController
                } else {
                    showErrorDialog("Update Failed", "No changes were made to the pet details.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while saving pet changes.");
        }
    }

    public static void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null);
        alert.setContentText(message); alert.showAndWait();
    }

    String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
    String user = "root";
    String password = "";

    private static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
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

