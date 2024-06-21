package Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
/**
 * This class is responsible for the logic of the scene where the player chooses the connection method.
 */
public class NetworkSceneController extends SceneController{
    /**
     * Constructor
     */
    public NetworkSceneController() {}
    /**
     * Buttons to choose the connection method
     */
    public RadioButton rmi_radio_button, socket_radio_button;
    /**
     * Button to go to the next scene
     */
    public Button next_button;

    /**
     * Set the connection method to RMI
     */
    @FXML
    public void setRMI(){
        rmi_radio_button.setSelected(true);
        socket_radio_button.setSelected(false);
        next_button.setDisable(false);
        next_button.setOpacity(1);
        controller.setRMIMode(true);
    }

    /**
     * Set the connection method to Socket
     */
    @FXML
    public void setSocket(){
        rmi_radio_button.setSelected(false);
        socket_radio_button.setSelected(true);
        next_button.setDisable(false);
        next_button.setOpacity(1);
        controller.setRMIMode(false);
    }

    /**
     * Go to the next scene
     */
    @FXML
    public void select_connection_method(){
        stage.setScene(sceneMap.get(SceneName.JOIN));
    }

}
