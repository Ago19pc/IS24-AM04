package run;

import Server.Card.Card;
import Server.Card.CardFace;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainBoardSceneController extends SceneController {
    @FXML
    public ImageView firstCardImage, secondCardImage, thirdCardImage, secretCardImage;
    @FXML
    public ImageView commonAchievement1, commonAchievement2;
    @FXML
    public ImageView goldOnDeck, goldOnFloor1, goldOnFloor2;
    @FXML
    public ImageView resourceOnDeck, resourceOnFloor1, resourceOnFloor2;
    @FXML
    public Button rotateFirstCardButton, rotateSecondCardButton, rotateThirdCardButton;
    @FXML
    public Group yourManuscript;

    private Face firstFace, secondFace, thirdFace;


    public void setHandCards() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0), Face.FRONT));
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), Face.FRONT));
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), Face.FRONT));
        firstFace = Face.FRONT;
        secondFace = Face.FRONT;
        thirdFace = Face.FRONT;
    }

    public void setSecretCard(Card card) {
        secretCardImage.setImage(getImageFromCard(card, Face.FRONT));
    }

    private Image getImageFromCard(Card card, Face face) {
        if (face == Face.FRONT) {
            return new Image(getClass().getResourceAsStream("/images/FrontFaces/" + card.getImageURI()));
        } else {
            return new Image(getClass().getResourceAsStream("/images/BackFaces/" + card.getImageURI()));
        }
    }

    private Image getImageFromCard(CardFace cardFace) {
        return new Image(getClass().getResourceAsStream("/images/BackFaces/" + cardFace.getImageURI()));
    }

    public void rotateFirstCard() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0), firstFace.getOpposite()));
        firstFace = firstFace.getOpposite();
    }

    public void rotateSecondCard() {
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), secondFace.getOpposite()));
        secondFace = secondFace.getOpposite();
    }

    public void rotateThirdCard() {
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), thirdFace.getOpposite()));
        thirdFace = thirdFace.getOpposite();
    }

    public void setCardOnFloor(Card card, Decks deck, DeckPosition position) {
        if (deck == Decks.GOLD) {
            switch (position) {
                case DECK:
                    goldOnDeck.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case FIRST_CARD:
                    goldOnFloor1.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case SECOND_CARD:
                    goldOnFloor2.setImage(getImageFromCard(card, Face.FRONT));
                    break;
            }
        } else if (deck == Decks.RESOURCE) {
            switch (position) {
                case DECK:
                    resourceOnDeck.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case FIRST_CARD:
                    resourceOnFloor1.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case SECOND_CARD:
                    resourceOnFloor2.setImage(getImageFromCard(card, Face.FRONT));
                    break;
            }

        } else if (deck == Decks.ACHIEVEMENT);
            switch (position) {
                case FIRST_CARD:
                    commonAchievement1.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case SECOND_CARD:
                    commonAchievement2.setImage(getImageFromCard(card, Face.FRONT));
                    break;
            }
    }

    public void updateAllFloorCards() {
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(0), Decks.GOLD, DeckPosition.DECK);
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(1), Decks.GOLD, DeckPosition.FIRST_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(2), Decks.GOLD, DeckPosition.SECOND_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(0), Decks.RESOURCE, DeckPosition.DECK);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(1), Decks.RESOURCE, DeckPosition.FIRST_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(2), Decks.RESOURCE, DeckPosition.SECOND_CARD);

        if (controller.getCommonAchievements() != null) {
            setCardOnFloor(controller.getCommonAchievements().get(0), Decks.ACHIEVEMENT, DeckPosition.FIRST_CARD);
            setCardOnFloor(controller.getCommonAchievements().get(1), Decks.ACHIEVEMENT, DeckPosition.SECOND_CARD);
        }
    }

    public void updateManuscript(CardFace cardFace, int x, int y) {
        ImageView imageView = new ImageView(getImageFromCard(cardFace));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setLayoutX(x*100);
        imageView.setLayoutY(y*100);
        yourManuscript.getChildren().add(imageView);



    }

}
