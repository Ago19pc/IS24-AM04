package run;

import Client.Controller.ClientController;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Interface for SceneController
 */
public abstract class SceneController {
    protected ClientController controller;
    protected Stage stage;
    protected Map<SceneName, Scene> sceneMap;
    protected SceneController sceneController;
    /**
     * Set all the necessary attributes for the SceneController
     * @param controller the ClientController
     * @param stage the stage
     * @param sceneMap the map of scenes
     */
    public void setAll(ClientController controller, Stage stage, Map<SceneName, Scene> sceneMap){
        this.controller = controller;
        this.stage = stage;
        this.sceneMap = sceneMap;
    }


}
