module it.am04.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;
    requires java.desktop;

    opens it.am04.demo1 to javafx.fxml;
    exports it.am04.demo1;
}