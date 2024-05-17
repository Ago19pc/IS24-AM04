package run;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class JoinSceneController extends SceneController{
    public TextField text_ip;
    public TextField text_Port;
    public Button join_Button;

    public void setJoin(ActionEvent actionEvent) {
        controller.joinServer(text_ip.getText(), Integer.parseInt(text_Port.getText()));

    }
}
