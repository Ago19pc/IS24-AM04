package run;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Enums.Face;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    public TextField messageToSend;
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

    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setup(){}
    /**
     * Returns the image of the card
     * @param card the card
     * @param face the face
     * @return the Image of the card
     */
    public Image getImageFromCard(Card card, Face face) {
        if (card == null) return new Image(getClass().getResourceAsStream("/images/Faces/back-96.jpeg"));
        return new Image(getClass().getResourceAsStream("/images/Faces/" + card.getFace(face).getImageURI()));
    }

    /**
     * This method is the same as the one above, but it takes a CardFace instead of a Face, so you can use the same method in different situations
     * Returns the image of the cardface
     * @param cardFace the cardface
     * @return the Image of the cardface
     */
    public Image getImageFromCard(CardFace cardFace) {
        if (cardFace == null) return new Image(getClass().getResourceAsStream("/images/Faces/back-96.jpeg"));
        return new Image(getClass().getResourceAsStream("/images/Faces/" + cardFace.getImageURI()));
    }

    /**
     * This method is called when the player wants to send a message in the chat
     */
    public void sendMessages() {
        controller.sendChatMessage(messageToSend.getText());
    }

}
