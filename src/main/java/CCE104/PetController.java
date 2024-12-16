package CCE104;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

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
    private TextField petAgeField;
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
        SpinnerValueFactory<Integer> ageFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 1);
        // Get the current Pet action from AppState
        AppState appState = AppState.getInstance();
        AppState.Pet currentPetPage = appState.getCurrentPetPage();
        if(currentPetPage != AppState.Pet.VIEW) {
            petAge.setValueFactory(ageFactory); }

        PetRecord selectedPet = PetRecord.getSelectedPet(); // Retrieve the selected pet

        // Check if the current pet action is not ADD
        if (currentPetPage != AppState.Pet.ADD) {
            if (selectedPet != null) {
                petName.setText(selectedPet.getName());
                petSpecies.setText(selectedPet.getSpecies());
                petBreed.setText(selectedPet.getBreed());
                if(currentPetPage != AppState.Pet.VIEW) {
                    petAge.getValueFactory().setValue(selectedPet.getPetAge()); }
                if(currentPetPage == AppState.Pet.VIEW) {
                    petAgeField.setText(Integer.toString(selectedPet.getPetAge())); }
                petOwnerID.setText(String.valueOf(selectedPet.getOwnerID()));
                petNotes.setText(selectedPet.getPetNotes());
                petImagePath = selectedPet.getPetImagePath();

                if (petImagePath != null && !petImagePath.isEmpty()) {
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(petImagePath)));
                    Circle clip = new Circle(75, 75, 75);
                    petImage.setClip(clip);
                    petImage.setImage(image);
                }
            }
        }
    }

    //database functions
    @FXML
    public void addPetToDatabase () throws IOException {
        try {
            // Validate inputs
            if(!validatePetInputs()){
                return;
            }

            String name = petName.getText().trim();
            String species = petSpecies.getText().trim();
            String breed = petBreed.getText().trim();
            Integer age = (Integer) petAge.getValue();
            String ownerID = petOwnerID.getText().trim();
            String notes = petNotes.getText().trim();

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
            String user = "root";
            String password = ""; // Replace with your password
            Connection connection = DriverManager.getConnection(url, user, password);

            // Insert data into Pets table
            String query = "INSERT INTO Pets (Name, Species, Breed, Age, OwnerID, PetNotes, PetImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, species);
            statement.setString(3, breed.isEmpty() ? null : breed);
            statement.setInt(4, age != null ? age : 0);
            statement.setInt(5, Integer.parseInt(ownerID));
            statement.setString(6, notes);
            statement.setString(7, petImagePath);

            if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to add pet? Please double check fields."))){
                return;
            }

            int rowsAffected = statement.executeUpdate();

            // Close the connection
            statement.close();
            connection.close();

            if (rowsAffected > 0) {
                showSuccessDialog("Success", "Pet added successfully.");
                clearPetFields();
            } else {
                System.out.println("Failed to add pet.");
            }
        } catch (IllegalArgumentException e) {
            showErrorDialog("Error", "Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An error occurred while adding pet.");
        }
    }

    private boolean validatePetInputs() {
        String name = petName.getText().trim();
        String species = petSpecies.getText().trim();
        String ownerID = petOwnerID.getText().trim();

        if (name.isEmpty()) {
            showAlert("Validation Error", "Pet name is required.");
            return false;
        }

        if (species.isEmpty()) {
            showAlert("Validation Error", "Species is required.");
            return false;
        }

        if (ownerID.isEmpty()) {
            showAlert("Validation Error", "Owner ID is required.");
            return false;
        }

        try {
            Integer.parseInt(ownerID); // Ensure Owner ID is a valid integer
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Owner ID must be a valid number.");
            return false;
        }

        if (petImagePath == null || petImagePath.isEmpty()) {
            showAlert("Validation Error", "Please select an image for the pet.");
            return false;
        }

        if (!isOwnerIDRegistered(ownerID)) {
            showAlert("Validation Error", "The provided OwnerID is not registered in the system.");
            return false;
        }
        return true;
    }

    private boolean isOwnerIDRegistered(String ownerID) {
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = "";
        String query = "SELECT COUNT(*) FROM Owners WHERE OwnerID = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ownerID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to validate OwnerID.");
        }

        return false;
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
        File initialDirectory = new File(projectRoot, "src/main/resources/CCE104/petImages");

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

    private void clearPetFields() {
        petName.setText("");
        petSpecies.setText("");
        petBreed.setText("");
        petNotes.setText("");
        petOwnerID.setText("");
        petImage.setImage(null);
    }

    @FXML
    public void savePetChanges () throws IOException, SQLException {
        try {
            if(!validatePetInputs()){
                return;
            }

            String name = petName.getText();
            String species = petSpecies.getText();
            String breed = petBreed.getText();
            Integer age = petAge.getValue();
            String ownerID = petOwnerID.getText();
            String path = petImagePath;
            String notes = petNotes.getText();

            // Step 2: Get the selected Pet ID from RecordsController
            RecordsController recordsController = PetRecord.getInstance().getRecordsController();
            Integer selectedPetID = recordsController.getSelectedPetID();
            if (selectedPetID == null) {
                showErrorDialog("No Pet Selected", "Please select a pet to edit.");
                return;
            }

            // Step 3: Update pet in the database
            String updateQuery = "UPDATE Pets SET Name = ?, Species = ?, Breed = ?, Age = ?, OwnerID = ?, PetNotes = ?, PetImagePath = ? WHERE PetID = ?";
            try (Connection conn = DriverManager.getConnection(url,user,password);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

                stmt.setString(1, name);
                stmt.setString(2, species);
                stmt.setString(3, breed);
                stmt.setInt(4, age);
                stmt.setInt(5, Integer.parseInt(ownerID));
                stmt.setString(6, notes);
                stmt.setString(7, path);
                stmt.setInt(8, selectedPetID);

                if(!(Alerts.showConfirmationDialog("Confirmation", "Are you sure to save pet changes? Please double check fields."))){
                    return;
                }

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showSuccessDialog("Success", "Pet details updated successfully.");
                    NavigationController.switchToRecordsWithFade();
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
        RecordsController recordsController = PetRecord.getInstance().getRecordsController();
        Integer selectedPetID = recordsController.getSelectedPetID();

        String name = petName.getText();
        String species = petSpecies.getText();
        String breed = petBreed.getText();
        Integer age = petAge.getValue();
        String ownerID = petOwnerID.getText();
        String path = petImagePath;
        String notes = petNotes.getText();
        Integer petID = selectedPetID;
    }

    @FXML
    public void addOwner () throws IOException {
        AppState.getInstance().setCurrentOwnerPage(AppState.Owner.ADD);
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

