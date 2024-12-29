package CCE104;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PopUpSwitcher {
    private static Stage currentPopupStage;

    //logger
    private static final Logger LOGGER = Logger.getLogger(PopUpSwitcher.class.getName());
    public static void showPopup(String fxmlPath, String title,
                                 boolean useFadeTransition,
                                 double fadeInDuration) {
        try {
            Parent newRoot = Main.loadFXML(fxmlPath);

            // Create a new stage for the popup
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);

            // If using fade transition, set initial opacity
            if (useFadeTransition) {
                newRoot.setOpacity(0);
            }

            // Create scene and set root
            stage.setScene(new Scene(newRoot));

            // Show the stage
            stage.show();

            // Apply fade-in transition if requested
            if (useFadeTransition) {
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(fadeInDuration), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }

            // Update current popup stage
            currentPopupStage = stage;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred", e);
        }
    }

    /**
     * Closes the current popup with optional fade-out transition
     *
     * @param useFadeTransition Whether to use fade-out transition
     * @param fadeOutDuration Duration of fade-out transition (in seconds)
     */
    public static void closeCurrentPopup(boolean useFadeTransition, double fadeOutDuration) {
        if (currentPopupStage != null) {
            if (useFadeTransition) {
                Parent root = currentPopupStage.getScene().getRoot();

                FadeTransition fadeOut = new FadeTransition(Duration.seconds(fadeOutDuration), root);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                fadeOut.setOnFinished(event -> {
                    currentPopupStage.close();
                    currentPopupStage = null;
                });

                fadeOut.play();
            } else {
                // Directly close without transition
                currentPopupStage.close();
                currentPopupStage = null;
            }
        }
    }

    /**
     * Checks if a popup is currently showing
     *
     * @return true if a popup is currently displayed, false otherwise
     */
    public static boolean isPopupShowing() {
        return currentPopupStage != null && currentPopupStage.isShowing();
    }
}