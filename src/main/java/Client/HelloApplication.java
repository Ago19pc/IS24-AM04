package Client;

import Client.Connection.ClientConnectionHandler;
import Client.Controller.ClientController;
import Client.View.CLI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {

    private static ClientController controller;
    private static CLI cli;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        controller = new ClientController();
        cli = new CLI(controller);
        controller.main(cli);
        cli.start();


        launch();
    }
}