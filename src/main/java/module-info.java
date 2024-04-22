module it.am04.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;
    requires java.desktop;
    requires com.google.gson;

    opens Client to javafx.fxml;
    exports Client;

    opens Server.GameModel to com.google.gson;
    opens Server.Deck to com.google.gson;
    opens Server.Enums to com.google.gson;
    opens Server.Player to com.google.gson;
    opens Server.Card to com.google.gson;
    opens Server.Chat to com.google.gson;
    opens Server.Manuscript to com.google.gson;
}