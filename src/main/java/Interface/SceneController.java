package Interface;

import Client.Controller.ClientController;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for SceneController, which contains the scene logic
 */
public abstract class SceneController implements Initializable {
    protected ClientController controller;
    protected Stage stage;
    protected Map<SceneName, Scene> sceneMap;
    protected SceneController sceneController;
    protected Map<SceneName,SceneController> sceneControllerMap = new HashMap<>();
    /**
     * Set all the necessary attributes for the SceneController
     * @param controller the ClientController
     * @param stage the stage
     * @param sceneMap the map of scenes
     * @param sceneControllerMap the map that links scene names to scene controllers
     */
    public void setAll(ClientController controller, Stage stage, Map<SceneName, Scene> sceneMap, Map<SceneName,SceneController> sceneControllerMap) {
        this.controller = controller;
        this.stage = stage;
        this.sceneMap = sceneMap;
        this.sceneControllerMap = sceneControllerMap;
    }

    /**
     * Initializes the controller
     * @param url the url for relative paths
     * @param resourceBundle the resources used to initialize the controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


}
