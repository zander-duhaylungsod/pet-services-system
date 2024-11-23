package CCE104;

import javafx.fxml.FXML;
import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSignin() throws IOException {
        Main.switchSceneWithFade("scenes/signin");
    }

    @FXML
    private void switchToSignup() throws IOException {
        Main.switchSceneWithFade("scenes/signUp");
    }
}