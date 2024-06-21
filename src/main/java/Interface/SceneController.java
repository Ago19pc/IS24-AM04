package Interface;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Enums.Face;
import javafx.application.Platform;
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
 * Abstract class for SceneController, which contains the scene logic
 */
public abstract class SceneController implements Initializable {
    ClientController controller;
    Stage stage;
    Map<SceneName, Scene> sceneMap;
    private SceneController sceneController;
    private Map<SceneName,SceneController> sceneControllerMap = new HashMap<>();
    public TextField messageToSend;
    /**
     * Constructor
     */
    public SceneController() {}
    /**
     * Set all the necessary attributes for the SceneController
     * @param controller the ClientController
     * @param stage the stage
     * @param sceneMap the map of scenes
     * @param sceneControllerMap the map that links scene names to scene controllers
     */
    public void setAll(ClientController controller, Stage stage, Map<SceneName, Scene> sceneMap, Map<SceneName,SceneController> sceneControllerMap) {
        this.controller = controller;
        this.stage = stage;
        this.sceneMap = sceneMap;
        this.sceneControllerMap = sceneControllerMap;
    }

    /**
     * Initializes the controller
     * @param url the url for relative paths
     * @param resourceBundle the resources used to initialize the controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Sets up the scene by doing initial operations
     */
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
        if (messageToSend.getText().equals("pasqualino")){
            Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).pasqualino());
        } else controller.sendChatMessage(messageToSend.getText());
    }

}
