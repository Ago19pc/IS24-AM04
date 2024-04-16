module it.am04.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;
    requires java.desktop;
    requires com.google.gson;

    opens Client to javafx.fxml;
    exports Client;
}