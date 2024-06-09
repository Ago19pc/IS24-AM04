package run;

import Client.Player;
import Client.View.OnBoardCard;
import Client.View.OtherPlayerTab;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Card.CornerCardFace;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import Server.Exception.PlayerNotFoundByNameException;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

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
    @FXML
    public Text yourTurnText;
    @FXML
    public List<OtherPlayerTab> otherPlayerTabs = new ArrayList<>();
    @FXML
    public Label Player1, Player2, Player3, Player4;

    private Face firstFace, secondFace, thirdFace;
    private int selectedCardIndex = -1;

    private Glow glow = new Glow(7);

    private Face selectedFace = null;





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
        return new Image(getClass().getResourceAsStream("/images/Faces/" + card.getFace(face).getImageURI()));
    }

    private Image getImageFromCard(CardFace cardFace) {
        return new Image(getClass().getResourceAsStream("/images/Faces/" + cardFace.getImageURI()));
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

    public void setBoardCards(Decks deck) {
        if (deck == Decks.GOLD) {

            goldOnDeck.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(0), Face.FRONT));
            goldOnFloor1.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(1), Face.FRONT));
            goldOnFloor2.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(2), Face.FRONT));
        } else if (deck == Decks.RESOURCE) {
            resourceOnDeck.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(0), Face.FRONT));
            resourceOnFloor1.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(1), Face.FRONT));
            resourceOnFloor2.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(2), Face.FRONT));
        } else if (deck == Decks.ACHIEVEMENT) {

            commonAchievement1.setImage(getImageFromCard(controller.getCommonAchievements().get(0), Face.FRONT));
            commonAchievement2.setImage(getImageFromCard(controller.getCommonAchievements().get(1), Face.FRONT));
        }
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

    public void updateManuscript(String name, CardFace cardFace, int x, int y) {
        if (name.equals(controller.getMyName())) {
            OnBoardCard card = new OnBoardCard(getImageFromCard(cardFace), x, y, null, this);
            card.place(yourManuscript);
        } else {
            // piazza la carta nel manoscritto di un altro
        }

    }

    public void firstCardSelected() {
        if (selectedCardIndex == 0) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 0;
            illuminateHandCard(0);
        }
    }

    public void secondCardSelected() {
        if (selectedCardIndex == 1) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 1;
            illuminateHandCard(1);
        }
    }

    public void thirdCardSelected() {
        if (selectedCardIndex == 2) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 2;
            illuminateHandCard(2);
        }
    }

    private void illuminateHandCard(int index) {
        switch (index) {
            case 0:
                firstCardImage.setEffect(glow);
                secondCardImage.setEffect(null);
                thirdCardImage.setEffect(null);
                selectedFace = firstFace;
                break;
            case 1:
                firstCardImage.setEffect(null);
                secondCardImage.setEffect(glow);
                thirdCardImage.setEffect(null);
                selectedFace = secondFace;
                break;
            case 2:
                firstCardImage.setEffect(null);
                secondCardImage.setEffect(null);
                thirdCardImage.setEffect(glow);
                selectedFace = thirdFace;
                break;
            default:
                firstCardImage.setEffect(null);
                secondCardImage.setEffect(null);
                thirdCardImage.setEffect(null);
                selectedFace = null;
        }

        if (selectedFace != null) {
            CornerCardFace face = (CornerCardFace) controller.getHand().get(selectedCardIndex).getFace(selectedFace);
            for (OnBoardCard c : OnBoardCard.onBoardCards) {
                try {
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x - 1, c.y - 1, face)) {
                        c.BOTTOM_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.BOTTOM_LEFT.setDisable(false);
                        System.out.println("CornerCardFace: " + face + "botton Bottom_Left enabled");
                    }
                    else {
                        c.BOTTOM_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.BOTTOM_LEFT.setDisable(true);
                        System.out.println("CornerCardFace: " + face + "botton Bottom_Left disabled");
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x + 1, c.y - 1, face)){
                        c.BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.BOTTOM_RIGHT.setDisable(false);
                        System.out.println("CornerCardFace: " + face + "botton Bottom_Right enabled");
                    }
                    else {
                        c.BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.BOTTOM_RIGHT.setDisable(true);
                        System.out.println("CornerCardFace: " + face + "botton Bottom_right disabled");
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x - 1, c.y + 1, face)){
                        c.TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.TOP_LEFT.setDisable(false);
                        System.out.println("CornerCardFace: " + face + "botton Top_Left enabled");
                    }
                    else {
                        c.TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.TOP_LEFT.setDisable(true);
                        System.out.println("CornerCardFace: " + face + "botton Top_Left disabled");
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x + 1, c.y + 1, face)){
                        c.TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.TOP_RIGHT.setDisable(false);
                        System.out.println("CornerCardFace: " + face + "botton Top_Right enabled");
                    }
                    else {
                        c.TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.TOP_RIGHT.setDisable(true);
                        System.out.println("CornerCardFace: " + face + "botton Top_Right disabled");
                    }
                } catch (PlayerNotFoundByNameException e) {
                    throw new RuntimeException(e);
                }

            }
        } else {
            for (OnBoardCard c : OnBoardCard.onBoardCards) {
                c.TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                c.TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                c.BOTTOM_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                c.BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
            }
        }
    }

    public void placeCard(int x, int y) {
        if (selectedCardIndex >= 0 && selectedCardIndex <= 2)
            controller.askPlayCard(selectedCardIndex, selectedFace, x, y);
    }

    @FXML
    public void drawFromGoldDeck() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.DECK);
    }

    @FXML
    public void drawFromResourceDeck() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.DECK);
    }

    @FXML
    public void drawFromGoldFloor1() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.FIRST_CARD);
    }

    @FXML
    public void drawFromGoldFloor2() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.SECOND_CARD);
    }

    @FXML
    public void drawFromResourceFloor1() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.FIRST_CARD);
    }

    @FXML
    public void drawFromResourceFloor2() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.SECOND_CARD);
    }

    public void removeCardFromHand(String playerName) {
        if (controller.getMyName().equals(playerName)) {
            switch (selectedCardIndex) {
                case 0:
                    firstCardImage.setImage(null);
                    break;
                case 1:
                    secondCardImage.setImage(null);
                    break;
                case 2:
                    thirdCardImage.setImage(null);
                    break;
            }
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        }
    }

    public void setup()
    {
        for(Player p : controller.getPlayers()) {
            if(!p.getName().equals(controller.getMyName())) {
                OtherPlayerTab tab = new OtherPlayerTab(p.getName());
                otherPlayerTabs.add(tab);
            }
        }
    }

    public void updateLeaderBoard() {
        List<Player> sorted = new ArrayList<Player>(controller.getPlayers());
        sorted.sort((p1, p2) -> p2.getPoints() - p1.getPoints());

    }

}
