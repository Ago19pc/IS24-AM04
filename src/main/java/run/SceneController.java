package run;

import Client.Controller.ClientController;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Interface for SceneController
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
     */
    public void setAll(ClientController controller, Stage stage, Map<SceneName, Scene> sceneMap, Map<SceneName,SceneController> sceneControllerMap) {
        this.controller = controller;
        this.stage = stage;
        this.sceneMap = sceneMap;
        this.sceneControllerMap = sceneControllerMap;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
