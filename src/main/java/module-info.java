/**
 * Codex Naturalis Game Project
 */
module it.am04codexnaturalis {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires com.google.gson;
    requires java.rmi;

    opens Client to javafx.fxml;


    exports Client;
    exports Client.Controller;
    exports Interface;
    exports Server;
    exports Server.Card;
    exports theMain;
    exports Server.Messages;
    exports Server.Connections to java.rmi;
    exports Client.Connection to java.rmi;

    opens Server.GameModel to com.google.gson;
    opens Server.Deck to com.google.gson;
    opens Server.Enums to com.google.gson;
    opens Server.Player to com.google.gson;
    opens Server.Card to com.google.gson;
    opens Server.Chat to com.google.gson;
    opens Server.Manuscript to com.google.gson;
    opens Interface to javafx.fxml;

}