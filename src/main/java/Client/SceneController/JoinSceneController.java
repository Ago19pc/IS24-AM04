package Client.SceneController;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class JoinSceneController extends SceneController{
    public TextArea text_ip;
    public TextArea text_Port;
    public Button join_Button;
    public Scene scene;

    public void setJoin(ActionEvent actionEvent) {
        controller.joinServer(text_ip.getText(), Integer.parseInt(text_Port.getText()));
        stage.setScene(sceneMap.get(SceneName.NAMECOLORREADY));
    }
}
