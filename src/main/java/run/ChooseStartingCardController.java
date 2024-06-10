package run;

import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Exception.PlayerNotFoundByNameException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import Server.Enums.Face;

import static Server.Enums.Face.BACK;
import static Server.Enums.Face.FRONT;

public class ChooseStartingCardController extends SceneController {
    @FXML
    public Button confirmButton;
    @FXML
    public ImageView firstCard, secondCard, chosenCard;
    @FXML
    public Text waitText;
    public Text backFaceText;
    public Text frontFaceText;
    public Text chosenCardText;
    public ListView<String> chat_message;
    public Button sendButton;
    @FXML
    private Face chosenFace;


    /**
     * Method to choose the first card face
     */
    public void chooseFirstCard() {
        chosenFace = FRONT;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        chosenCardText.setText("Back Face");
        chosenCard.setImage(firstCard.getImage());
    }

    /**
     * Method to choose the second card face
     */
    public void chooseSecondCard() {
        chosenFace = BACK;
        confirmButton.setDisable(false);
        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        chosenCardText.setText("Front Face");
        chosenCard.setImage(secondCard.getImage());
    }


    /**
     * Method to confirm the chosen card face
     */
    public void confirmationButtonPressed() {
        controller.chooseStartingCardFace(chosenFace);
    }

    /**
     * This is called when the server confirms the chosen card
     */
    public void confirmation() {
        waitText.setVisible(true);
        firstCard.setOpacity(0);
        firstCard.setDisable(true);
        secondCard.setOpacity(0);
        secondCard.setDisable(true);
        confirmButton.setVisible(false);
        confirmButton.setDisable(true);
        backFaceText.setOpacity(0);
        frontFaceText.setOpacity(0);
        chosenCardText.setOpacity(1);
        chosenCardText.setText("Chosen Card");
        chosenCard.setLayoutX(414);
        chosenCard.setLayoutY(264);
        chosenCard.setOpacity(1);
        try {
            CornerCardFace chosenCardFace = controller.getPlayerByName(controller.getMyName()).getManuscript().getCardByCoord(0, 0);
            chosenCard.setImage(new Image(getClass().getResource("/images/Faces/"+ chosenCardFace.getImageURI()).toExternalForm()));
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to set up the scene
     * @param card the card to be displayed
     */
    public void setUp(Card card) {
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        firstCard.setImage(new Image(getClass().getResource("/images/Faces/"+ card.getFace(FRONT).getImageURI()).toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/Faces/"+ card.getFace(BACK).getImageURI()).toExternalForm()));
        waitText.setVisible(false);
        chosenCardText.setOpacity(0);
        chosenCard.setOpacity(0);
    }
}
