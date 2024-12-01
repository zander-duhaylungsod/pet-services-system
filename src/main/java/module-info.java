module CCE104 {
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires javafx.web;
    requires java.desktop;
    requires javafx.controls;

    opens CCE104 to javafx.fxml;
    exports CCE104;
}
