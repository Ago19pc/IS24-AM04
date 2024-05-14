package Client;

import Client.Controller.ClientController;
import Client.View.CLI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class HelloApplication extends Application {

    private static ClientController controller;
    private static CLI cli;

    Scene scene1, scene2;
    Stage window;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
    //   Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        window = primaryStage;
        Button button1 = new Button("LOGIN");
        button1.setOnAction(e -> window.setScene(scene2));

        //Layout 1
        StackPane layout1 = new StackPane();
        layout1.getChildren().addAll(button1);
        scene1 = new Scene(layout1, 200, 200);

        Button button2 = new Button("Don't click me");
        button2.setOnAction(e -> window.setScene(scene1));

        //Layout 2
        VBox layout2 = new VBox(20);
        layout2.getChildren().addAll(button2);
        scene2 = new Scene(layout2, 200, 200);

        window.setTitle("Hello!");
        window.setScene(scene1);
        window.show();
    }

    public static void main(String[] args) throws RemoteException {

        controller = new ClientController();
        cli = new CLI(controller);
        controller.main(cli);
        cli.start();



        launch();
    }
}