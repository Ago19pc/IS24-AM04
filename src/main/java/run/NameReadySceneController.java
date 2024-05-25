package run;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class NameReadySceneController extends SceneController{

    @FXML
    public  Pane loginScene;
    @FXML
    public  Label label_title;
    @FXML
    public  Pane user_pane;
    @FXML
    public TextField possible_Name;
    @FXML
    public  Button confirm_Button;
    @FXML
    public  Label label_Username;
    @FXML
    public ListView<String> list_Player;
    public ListView<String> chat_message;

    public void askSetName(ActionEvent actionEvent) {
        controller.askSetName(possible_Name.getText());
    }

    public void cloneListView(ListView<String> other) {
        list_Player.getItems().clear();
        for (String item : other.getItems()) {
            list_Player.getItems().add(item);
        }
    }
}
