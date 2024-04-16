package Client;

import Client.Connection.ClientConnectionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {

    private static ClientConnectionHandler connectionHandler;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Inserisci l'indirizzo IP del server");
        String ip = inputReader.nextLine();
        System.out.println("Inserisci la porta del server");
        int port = inputReader.nextInt();
        connectionHandler = new ClientConnectionHandler(ip, port);






        launch();
    }
}