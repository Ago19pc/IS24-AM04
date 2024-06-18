package Interface;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

/**
 * This class is responsible for the logic of the scene where the player joins the server.
 */
public class JoinSceneController extends SceneController{
    public TextField text_ip;
    public TextField text_Port;
    public Button join_Button;

    /**
     * Handles the join button click event
     */
    public void setJoin() {
        if(Objects.equals(text_ip.getText(), "")) {
            controller.joinServer("localhost", 1234);
        }
        else controller.joinServer(text_ip.getText(), Integer.parseInt(text_Port.getText()));

    }
}
