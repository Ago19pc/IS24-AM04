package run;

import Server.Card.AchievementCard;
import Server.Enums.Decks;
import Server.Enums.Face;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChooseSecretCardController extends SceneController {
    @FXML
    public Button confirmButton;
    @FXML
    public ImageView firstCard, secondCard;
    public Text firstCardText;
    public Text secondCardText;
    public ImageView thirdCardInHand;
    public ImageView secondCardInHand;
    public ImageView firstCardInHand;
    public ImageView goldCard1;
    public ImageView goldDeckCard;
    public ImageView objective2;
    public ImageView resourceCard2;
    public ImageView resourceCard1;
    public ImageView resourceDeckCard;
    public ImageView goldCard2;
    public ImageView objective1;
    public Button sendButton;
    public ListView<String> chat_message;

    private int index = 0;

    /**
     * This method is called when the first card is chosen
     */
    public void chooseFirstCard() {
        index = 0;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        confirmButton.setOpacity(1);
    }

    /**
     * This method is called when the second card is chosen
     */
    public void chooseSecondCard() {
        index = 1;
        confirmButton.setDisable(false);
        confirmButton.setOpacity(1);
        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
    }

    /**
     * This method is called when the server confirms the choice, and now you have to wait the other players
     */
    public void confirmation() {
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        index = controller.getIndexofSecretAchievement();
    }

    /**
     * This method is called when the confirmation button is pressed
     */
    public void confirmationButtonPressed() {
        controller.chooseSecretAchievement(index);
    }


    /**
     * This method is called when the scene is loaded
     * @param cards the list of cards to be displayed
     */
    public void setUp(List<AchievementCard> cards) {
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        firstCard.setImage(new Image(getClass().getResource("/images/Faces/" + cards.get(0).getFace(Face.FRONT).getImageURI()).toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/Faces/" + cards.get(1).getFace(Face.FRONT).getImageURI()).toExternalForm()));
        firstCardInHand.setImage(getImageFromCard(controller.getHand().get(0), Face.FRONT));
        secondCardInHand.setImage(getImageFromCard(controller.getHand().get(1), Face.FRONT));
        thirdCardInHand.setImage(getImageFromCard(controller.getHand().get(2), Face.FRONT));
        objective1.setImage(getImageFromCard(controller.getCommonAchievements().get(0), Face.FRONT));
        objective2.setImage(getImageFromCard(controller.getCommonAchievements().get(1), Face.FRONT));
        resourceDeckCard.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(0), Face.FRONT));
        resourceCard1.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(1), Face.FRONT));
        resourceCard2.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(2), Face.FRONT));
        goldDeckCard.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(0), Face.FRONT));
        goldCard1.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(1), Face.FRONT));
        goldCard2.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(2), Face.FRONT));
    }
}
