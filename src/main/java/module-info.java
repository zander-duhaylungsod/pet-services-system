module CCE104 {
    requires javafx.controls;
    requires javafx.fxml;

    opens CCE104 to javafx.fxml;
    exports CCE104;
}
