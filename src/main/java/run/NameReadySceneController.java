package run;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class NameReadySceneController extends SceneController{


    public Pane loginScene;
    public Label label_title;
    public Pane user_pane;
    public TextField possible_Name;
    public Button confirm_Button;
    public Label label_Username;

    public void askSetName(ActionEvent actionEvent) {
        controller.askSetName(possible_Name.getText());
    }
}
