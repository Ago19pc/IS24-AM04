package Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * This class is responsible for the logic of the scene where the player chooses the name and sets ready (in order to reconnect to a game).

 */
public class NameReadySceneController extends SceneController{
    /**
     * Constructor
     */
    public NameReadySceneController() {
    }
    /**
     * The pane of the scene
     */
    @FXML
    public  Pane loginScene;
    /**
     * The text field where the user inserts the name
     */
    @FXML
    public TextField possible_Name;
    /**
     * The button to confirm the name
     */
    @FXML
    public  Button confirm_Button;
    /**
     * The label to show the username
     */
    @FXML
    public  Label label_Username;
    /**
     * The list of players
     */
    @FXML
    public ListView<String> list_Player;
    /**
     * The list of messages in the chat
     */
    public ListView<String> chat_message;
    /**
     * The label to show the reconnection id
     */
    public Label labelReconnectionId;
    /**
     * The text field where the user inserts the reconnection id
     */
    public TextField reconnection_idField;
    /**
     * The button to confirm the reconnection id
     */
    public Button confirm_ButtonId;

    /**
     * This method is called when the users insert their name, but still need confirmation from server
     */
    public void askSetName() {
        System.out.println("Name Field: " + possible_Name.getText());
        if (controller.isSavedGame()) {
            controller.joinSavedGame(possible_Name.getText());
        } else controller.askSetName(possible_Name.getText());
        stage.setTitle("Codex Naturalis - " + possible_Name.getText());
    }

    /**
     * This method ic called to clone the list of players from the other scene
     * @param other, the list of players
     */
    public void cloneListView(ListView<String> other) {
        list_Player.getItems().clear();
        for (String item : other.getItems()) {
            list_Player.getItems().add(item);
        }
    }

    /**
     * This method setups the scene
     */
    public void setup(){
        labelReconnectionId.setText("New reconnection ID: " + controller.getMyId() + " save it for reconnection");
        reconnection_idField.setPromptText("Enter the old reconnection ID");
        possible_Name.setPromptText("Choose a name");
    }

    /**
     * This method is called when the user wants to reconnect to a game
     */
    public void idLogin() {
        System.out.println("Id Field: "+reconnection_idField.getText());
        controller.reconnect(reconnection_idField.getText());
    }
}
