package CCE104;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("scenes/dashboardAdmin"));
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
            // Set the new root after fade-out is complete
            scene.setRoot(newRoot);

            // Start the fade-in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
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
        String url = "jdbc:mysql://localhost:3306/syntaxSquad_db"; // Update with your database name
        String user = "root"; // Default username for XAMPP
        String password = ""; // Default password (leave empty if not set)

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