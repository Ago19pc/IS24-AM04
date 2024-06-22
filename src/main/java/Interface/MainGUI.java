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
        gui.generateScenes(stage);
        stage.setTitle("Welcome to Codex Naturalis!");
        gui.askConnectionMode();
        controller.main( gui);

    }
}
