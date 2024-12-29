package CCE104;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {
    public static void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null);
        alert.setContentText(message); alert.showAndWait();
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showErrorDialog(String title, String content) {
        showAlert(title, content);
    }

    public static boolean showConfirmationDialog(String title, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Return true if the user clicked "OK", false otherwise (including "Cancel" or closing the dialog)
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
