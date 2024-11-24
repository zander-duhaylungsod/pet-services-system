module CCE104 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;

    opens CCE104 to javafx.fxml;
    exports CCE104;
}
