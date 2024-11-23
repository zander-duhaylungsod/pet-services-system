package CCE104;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToSignup() throws IOException {
        Main.setRoot("scenes/primary");
    }
}