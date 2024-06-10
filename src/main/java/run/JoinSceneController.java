package run;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class JoinSceneController extends SceneController{
    public TextField text_ip;
    public TextField text_Port;
    public Button join_Button;

    /**
     * LITERALLY THE JOIN BUTTON TO CONNECT TO THE SERVER, WHAT DID YOU EXPECT?
     * @param actionEvent
     */
    public void setJoin(ActionEvent actionEvent) {
        if(Objects.equals(text_ip.getText(), "")) {
            controller.joinServer("localhost", 1234);
        }
        else controller.joinServer(text_ip.getText(), Integer.parseInt(text_Port.getText()));

    }
}
