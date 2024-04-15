module it.am04.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;
    requires java.desktop;
    requires com.google.gson;

    opens it.am04.demo1 to javafx.fxml;
    exports it.am04.demo1;
}