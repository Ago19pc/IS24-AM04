package main.java.GameModel;

import main.java.Card.RegularBackFace;
import main.java.Card.StartingCard;
import main.java.Card.StartingFrontFace;
import main.java.Chat.Chat;
import main.java.Deck.AchievementDeck;
import main.java.Deck.GoldDeck;
import main.java.Deck.ResourceDeck;
import main.java.Player.Player;

import java.util.List;

public class GameModelInstance implements GameModel {
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private AchievementDeck achievementDeck;
    private List<StartingCard> startingCards;
    private int turn;
    private boolean isEndGamePhase;
    private Chat chat;
    private List<Player> playerList;

    public GameModelInstance() {
        System.out.println(this.toString() + "GameModelInstance");
        resourceDeck = new ResourceDeck();
        goldDeck = new GoldDeck();
        achievementDeck = new AchievementDeck();
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        for (int i = 0; i < 5; i++){
            StartingFrontFace frontFace = new StartingFrontFace("imageURI", null, null);
            RegularBackFace backFace = new RegularBackFace("imageURI", null);
            startingCards.add(new StartingCard());
        }
        Collection.shuffle(startingCards);
    }

    /**
     * Adds 1 to turn
     */
    @Override
    public void nextTurn() {
        this.turn++;
    }

    /**
     * @return ResourceDeck the resource deck
     */
    @Override
    public ResourceDeck getResourceDeck() {
        return this.resourceDeck;
    }

    /**
     * @return GoldDeck the gold deck
     */
    @Override
    public GoldDeck getGoldDeck() {
        return this.goldDeck;
    }

    /**
     * @return AchievementDeck the achievement deck
     */
    @Override
    public AchievementDeck getAchievementDeck() {
        return this.achievementDeck;
    }

    /**
     * @return List<StartingCard> the starting cards
     */
    @Override
    public List<StartingCard> getStartingCards() {
        return this.startingCards;
    }

    /**
     * @return int the turn
     */
    @Override
    public int getTurn() {
        return this.turn;
    }

    /**
     * @return boolean true if the game is in the end game phase
     */
    @Override
    public boolean isEndGamePhase() {
        return this.isEndGamePhase;
    }

    /**
     * Set the game to the end game phase
     *
     * @param endGamePhase boolean true or false
     */
    @Override
    public void setEndGamePhase(boolean endGamePhase) {
        this.isEndGamePhase = endGamePhase;
    }

    /**
     * @return Chat the chat
     */
    @Override
    public Chat getChat() {
        return this.chat;
    }
}
