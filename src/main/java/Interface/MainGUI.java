package Interface;

import Client.Controller.ClientController;
import Client.Controller.ClientControllerInstance;
import Client.View.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is responsible for starting the GUI.
 */
public class MainGUI extends Application {
    private static ClientController controller;
    private static GUI gui;

    /**
     * Constructor
     */
    public MainGUI() {}

    /**
     * Starts the GUI and launches JavaFX
     */
    public static void main()  {
        controller = new ClientControllerInstance();
        gui = new GUI(controller);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        gui.setStage(stage);
        gui.generateScene("/rmi_or_socket.fxml", SceneName.NETWORK, stage);
        gui.generateScene("/graficap1.fxml", SceneName.JOIN, stage);
        gui.generateScene("/setName.fxml", SceneName.SETNAME, stage);
        gui.generateScene("/setColor.fxml", SceneName.SETCOLOR, stage);
        gui.generateScene("/chooseStartingCard.fxml", SceneName.STARTINGCARDCHOICE, stage);
        gui.generateScene("/chooseSecretCard.fxml", SceneName.SECRETCARDCHOICE, stage);
        gui.generateScene("/mainboard.fxml", SceneName.GAME, stage);
        stage.setTitle("Welcome to Codex Naturalis!");
        gui.askConnectionMode();
        controller.main( gui);

    }
}
