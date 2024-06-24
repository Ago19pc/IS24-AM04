package Interface;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the logic of the main board scene
 */
public class MainBoardSceneController extends SceneController {

    /**
     * Constructor
     */
    public MainBoardSceneController() {
        super();
    }

    /**
     * The images of the player hand and secret achievement cards
     */
    @FXML
    public ImageView firstCardImage, secondCardImage, thirdCardImage, secretCardImage;
    /**
     * The images of the common achievements
     */
    @FXML
    public ImageView commonAchievement1, commonAchievement2;
    /**
     * The images of the gold deck cards
     */
    @FXML
    public ImageView goldOnDeck, goldOnFloor1, goldOnFloor2;
    /**
     * The images of the resource deck cards
     */
    @FXML
    public ImageView resourceOnDeck, resourceOnFloor1, resourceOnFloor2;
    /**
     * The buttons to rotate the cards in the hand to show the other faces
     */
    @FXML
    public Button rotateFirstCardButton, rotateSecondCardButton, rotateThirdCardButton;
    /**
     * The button to go back to the lobby
     */
    @FXML
    public Button backToLobbyButton;
    /**
     * The player's manuscript
     */
    @FXML
    public Group yourManuscript;
    /**
     * The label to show that it's the player's turn
     */
    @FXML
    public Label yourTurnLabel;
    /**
     * The label to show the end phase
     */
    @FXML
    public Label endingLabel;
    /**
     * The Tabs to show the other players' manuscripts
     */
    @FXML
    public TabPane tabPane;
    /**
     * The button to send a chat message
     */
    @FXML
    public Button chatButton;
    /**
     * The list of chat messages
     */
    @FXML
    public ListView<String> chatMessages;
    /**
    Maps the player name to the tab of the player
     */
    public final Map<String, OtherPlayerTab> otherPlayerTabs = new HashMap<>();
    /**
     * The player names
     */
    @FXML
    public Label Player1, Player2, Player3, Player4;
    /**
     * The count of how many cards are left in the gold deck
     */
    public Label goldDeck_counter;
    /**
     * The count of how many cards are left in the resource deck
     */
    public Label resourceDeck_counter;

    private Face firstFace, secondFace, thirdFace;
    private int selectedCardIndex = -1;

    private final Glow glow = new Glow(7);

    private Face selectedFace = null;

    /**
     * Decreases the counter of the gold deck
     */
    public void decrementGoldDeckCounter() {
        int counter = Integer.parseInt(goldDeck_counter.getText());
        goldDeck_counter.setText(String.valueOf(counter - 1));
    }

    /**
     * Decreases the counter of the resource deck
     */
    public void decrementResourceDeckCounter() {
        int counter = Integer.parseInt(resourceDeck_counter.getText());
        resourceDeck_counter.setText(String.valueOf(counter - 1));
    }
    /**
     * Displays the hand cards of the player
     */
    public void setHandCards() {

        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0), Face.FRONT));
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), Face.FRONT));
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), Face.FRONT));
        firstFace = Face.FRONT;
        secondFace = Face.FRONT;
        thirdFace = Face.FRONT;
    }

    /**
     * Displays the secret card of the player
     * @param card the secret card
     */
    public void setSecretCard(Card card) {
        secretCardImage.setImage(getImageFromCard(card, Face.FRONT));
    }

    /**
     * Rotates the first card in hand to see the other face
     */
    public void rotateFirstCard() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().getFirst(), firstFace.getOpposite()));
        firstFace = firstFace.getOpposite();
    }

    /**
     * Rotates the second card in hand to see the other face
     */
    public void rotateSecondCard() {
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), secondFace.getOpposite()));
        secondFace = secondFace.getOpposite();
    }

    /**
     * Rotates the third card in hand to see the other face
     */
    public void rotateThirdCard() {
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), thirdFace.getOpposite()));
        thirdFace = thirdFace.getOpposite();
    }

    /**
     * Sets the board cards, the gold/resource/achievement ones
     * @param deck the deck you want to set
     */
    public void setBoardCards(Decks deck) {
        if (deck == Decks.GOLD) {

            goldOnDeck.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(0), Face.BACK));
            goldOnFloor1.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(1), Face.FRONT));
            goldOnFloor2.setImage(getImageFromCard(controller.getBoardCards(Decks.GOLD).get(2), Face.FRONT));
        } else if (deck == Decks.RESOURCE) {
            resourceOnDeck.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(0), Face.BACK));
            resourceOnFloor1.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(1), Face.FRONT));
            resourceOnFloor2.setImage(getImageFromCard(controller.getBoardCards(Decks.RESOURCE).get(2), Face.FRONT));
        } else if (deck == Decks.ACHIEVEMENT) {

            commonAchievement1.setImage(getImageFromCard(controller.getCommonAchievements().get(0), Face.FRONT));
            commonAchievement2.setImage(getImageFromCard(controller.getCommonAchievements().get(1), Face.FRONT));
        }
    }

    /**
     * Sets the card on the floor
     * @param card the card you want to set
     * @param deck the deck you want to set (gold - resource - achievement)
     * @param position the position you want to set (DECK - FIRST_CARD - SECOND_CARD)
     */
    public void setCardOnFloor(Card card, Decks deck, DeckPosition position) {
        if (deck == Decks.GOLD) {
            switch (position) {
                case DECK:
                    goldOnDeck.setImage(getImageFromCard(card, Face.BACK));
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
                    resourceOnDeck.setImage(getImageFromCard(card, Face.BACK));
                    break;
                case FIRST_CARD:
                    resourceOnFloor1.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case SECOND_CARD:
                    resourceOnFloor2.setImage(getImageFromCard(card, Face.FRONT));
                    break;
            }

        } else if (deck == Decks.ACHIEVEMENT) {
            switch (position) {
                case FIRST_CARD:
                    commonAchievement1.setImage(getImageFromCard(card, Face.FRONT));
                    break;
                case SECOND_CARD:
                    commonAchievement2.setImage(getImageFromCard(card, Face.FRONT));
                    break;
            }
        }
    }

    /**
     * Updates all the floor cards
     * Gold, Resource and Achievement
     */
    public void updateAllFloorCards() {
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(0), Decks.GOLD, DeckPosition.DECK);
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(1), Decks.GOLD, DeckPosition.FIRST_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.GOLD).get(2), Decks.GOLD, DeckPosition.SECOND_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(0), Decks.RESOURCE, DeckPosition.DECK);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(1), Decks.RESOURCE, DeckPosition.FIRST_CARD);
        setCardOnFloor(controller.getBoardCards(Decks.RESOURCE).get(2), Decks.RESOURCE, DeckPosition.SECOND_CARD);

        if (!controller.getCommonAchievements().isEmpty()) {
            setCardOnFloor(controller.getCommonAchievements().get(0), Decks.ACHIEVEMENT, DeckPosition.FIRST_CARD);
            setCardOnFloor(controller.getCommonAchievements().get(1), Decks.ACHIEVEMENT, DeckPosition.SECOND_CARD);
        }
    }

    /**
     * Updates the manuscript of the player
     * This method is called to place a new card
     * @param name the name of the player, useful to find the correct manuscript to place
     * @param cardFace the cardface
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void updateManuscript(String name, CardFace cardFace, int x, int y) {
        if (name.equals(controller.getMyName())) {
            OnBoardCard card = new OnBoardCard(getImageFromCard(cardFace), x, y, this);
            card.place(yourManuscript);
        } else {
            OtherPlayerTab tab = otherPlayerTabs.get(name);
            if (tab != null) {
                OnBoardCard card = new OnBoardCard(getImageFromCard(cardFace), x, y, this);
                tab.placeCard(card);
            }
        }

    }

    /**
     * This is called when a player selects/deselects the first card in hand
     */
    public void firstCardSelected() {
        if (selectedCardIndex == 0) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 0;
            illuminateHandCard(0);
        }
    }

    /**
     * This is called when a player selects/deselects the second card in hand
     */
    public void secondCardSelected() {
        if (selectedCardIndex == 1) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 1;
            illuminateHandCard(1);
        }
    }

    /**
     * This is called when a player selects/deselects the third card in hand
     */
    public void thirdCardSelected() {
        if (selectedCardIndex == 2) {
            selectedCardIndex = -1;
            illuminateHandCard(-1);
        } else {
            selectedCardIndex = 2;
            illuminateHandCard(2);
        }
    }

    /**
     * This method is called to illuminate the selected card
     * @param index the index of the card in the hand position counting from 0
     */
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
                    }
                    else {
                        c.BOTTOM_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.BOTTOM_LEFT.setDisable(true);
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x + 1, c.y - 1, face)){
                        c.BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.BOTTOM_RIGHT.setDisable(false);
                    }
                    else {
                        c.BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.BOTTOM_RIGHT.setDisable(true);
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x - 1, c.y + 1, face)){
                        c.TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.TOP_LEFT.setDisable(false);
                    }
                    else {
                        c.TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.TOP_LEFT.setDisable(true);
                    }
                    if(controller.getPlayerByName(controller.getMyName()).getManuscript().isPlaceable(c.x + 1, c.y + 1, face)){
                        c.TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: gold;");
                        c.TOP_RIGHT.setDisable(false);
                    }
                    else {
                        c.TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                        c.TOP_RIGHT.setDisable(true);
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

    /**
     * This method is called when the player places a card
     * this method calls the controller to place the card
     * which will ask the server to place the card
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void placeCard(int x, int y) {
        if (selectedCardIndex >= 0 && selectedCardIndex <= 2)
            controller.askPlayCard(selectedCardIndex, selectedFace, x, y);
    }

    /**
     * This method is called when the player wants to draw a gold card from the gold deck
     */
    @FXML
    public void drawFromGoldDeck() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.DECK);
    }

    /**
     * This method is called when the player wants to draw a resource card from the resource deck
     */
    @FXML
    public void drawFromResourceDeck() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.DECK);
    }

    /**
     * This method is called when the player wants to draw a gold card from the first position on the floor
     */
    @FXML
    public void drawFromGoldFloor1() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.FIRST_CARD);
    }

    /**
     * This method is called when the player wants to draw a gold card from the second position on the floor
     */
    @FXML
    public void drawFromGoldFloor2() {
        controller.askDrawCard(Decks.GOLD, DeckPosition.SECOND_CARD);
    }

    /**
     * This method is called when the player wants to draw a resource card from the first position on the floor
     */
    @FXML
    public void drawFromResourceFloor1() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.FIRST_CARD);
    }

    /**
     * This method is called when the player wants to draw a resource card from the second position on the floor
     */
    @FXML
    public void drawFromResourceFloor2() {
        controller.askDrawCard(Decks.RESOURCE, DeckPosition.SECOND_CARD);
    }

    /**
     * This method removes the card from the player hand
     * @param playerName the name of the player
     */
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

    /**
     * Generates the Tabs to see the other player's manuscript
     */
    public void setup()
    {
        backToLobbyButton.setDisable(true);
        for(Player p : controller.getPlayers()) {
            if(!p.getName().equals(controller.getMyName())) {
                OtherPlayerTab tab = new OtherPlayerTab(p.getName());
                if (!tabPane.getTabs().stream().map(Tab::getText).toList().contains(p.getName())) {
                    otherPlayerTabs.put(p.getName(), tab);
                    tabPane.getTabs().add(tab.tab);
                }
            }
        }
        updateLeaderBoard();
    }
    /**
     * Sets the Go Back to Lobby button to be enabled
     */
    public void enableBackToLobbyButton() {
        backToLobbyButton.setDisable(false);
    }

    /**
     * Updates the player order in the leaderboard tab
     */
    public void updateLeaderBoard() {
        System.out.println("Leaderboard updated");
        List<Player> sorted = new ArrayList<>(controller.getPlayers());
        sorted.sort((p1, p2) -> p2.getPoints() - p1.getPoints());

        String player2Name;
        int player2Points;
        try {
            player2Name = sorted.get(1).getName();
            player2Points = sorted.get(1).getPoints();
        } catch (IndexOutOfBoundsException e) {
            player2Name = "[UNAVAILABLE PLAYER]";
            player2Points = 0;
        }

        Player1.setText("Player: " + sorted.get(0).getName() + " Points: " + sorted.get(0).getPoints());
        Player2.setText("Player: " + player2Name + " Points: " + player2Points);
        if (sorted.size() > 2) {
            Player3.setText("Player: " + sorted.get(2).getName() + " Points: " + sorted.get(2).getPoints());
        } else {
            Player3.setText("");
        }
        if (sorted.size() > 3) {
            Player4.setText("Player: " + sorted.get(3).getName() + " Points: " + sorted.get(3).getPoints());
        } else {
            Player4.setText("");
        }

    }

    public void pasqualino() {
        Tab tab = new Tab("Squadra Genew");
        AnchorPane pane = new AnchorPane();
        ImageView image = new ImageView(getClass().getResource("/images/otherImages/uovadipasqua.jpg").toExternalForm());
        image.setPreserveRatio(false);
        image.setFitWidth(400);
        image.setFitHeight(300);
        pane.getChildren().add(image);
        tab.setContent(pane);
        tabPane.getTabs().add(tab);
    }


    public void backToLobby() {
        controller.clearUI();
    }
}
