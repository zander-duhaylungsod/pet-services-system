package CCE104;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static Scene scene;
    //logger
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("scenes/signIn"));
        stage.setScene(scene);
        stage.show();
    }

    static void switchSceneWithFade(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);

        // Set the new root's opacity to 0 before starting the transition
        newRoot.setOpacity(0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            scene.setRoot(newRoot);

            // Start the fade-in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    public static void showPopup(String fxmlPath, String title) {
        try {
            Parent newRoot = loadFXML(fxmlPath);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(newRoot));
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    static void switchScene(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);
        scene.setRoot(newRoot);
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db";
        String user = "root";
        String password = "";

        try {
            // Establishing the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database established successfully!");

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        launch();
    }
}