package run;

import Client.Controller.ClientController;
import Client.View.GUI;
import Client.View.UI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class MainGUI extends Application {
    private static ClientController controller;
    private static NameColorReadySceneController nameColorReadySceneController;
    private static GUI gui;
    public static void main(String[] args) throws RemoteException {
        controller = new ClientController();
        nameColorReadySceneController = new NameColorReadySceneController();
        gui = new GUI(controller , nameColorReadySceneController);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        gui.setStage(stage);
        gui.generateScene("rmi_or_socket.fxml", SceneName.NETWORK, stage);
        gui.generateScene("graficap1.fxml", SceneName.JOIN, stage);
        gui.generateScene("setName.fxml", SceneName.SETNAME, stage);
        stage.setTitle("Hello!");
        gui.askConnectionMode();
        controller.main((UI) gui);

    }
}
