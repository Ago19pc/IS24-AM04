package run;

import Client.Controller.ClientController;
import Client.View.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUI extends Application {
    private static ClientController controller;
    private static GUI gui;
    public static void main(String[] args)  {
        controller = new ClientController();
        gui = new GUI(controller);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        gui.setStage(stage);
        gui.generateScene("rmi_or_socket.fxml", SceneName.NETWORK, stage);
        gui.generateScene("graficap1.fxml", SceneName.JOIN, stage);
        gui.generateScene("setName.fxml", SceneName.SETNAME, stage);
        gui.generateScene("setColor.fxml", SceneName.SETCOLOR, stage);
        stage.setTitle("Hello!");
        gui.askConnectionMode();
        controller.main( gui);

    }
}
