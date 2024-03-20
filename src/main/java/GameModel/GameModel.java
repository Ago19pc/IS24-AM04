package main.java.GameModel;

import main.java.Card.StartingCard;
import main.java.Deck.AchievementDeck;
import main.java.Deck.GoldDeck;
import main.java.Deck.ResourceDeck;
import main.java.Chat.Chat;

import java.util.List;

public interface GameModel {
    /**
     * Adds 1 to turn
     */
    public void nextTurn();

    /**
     * @return ResourceDeck the resource deck
     */
    public ResourceDeck getResourceDeck();

    /**
     * @return GoldDeck the gold deck
     */
    public GoldDeck getGoldDeck();

    /**
     * @return AchievementDeck the achievement deck
     */
    public AchievementDeck getAchievementDeck();

    /**
     * @return List<StartingCard> the starting cards
     */
    public List<StartingCard> getStartingCards();

    /**
     * @return int the turn
     */
    public int getTurn();

    /**
     * @return boolean true if the game is in the end game phase
     */
    public boolean isEndGamePhase();

    /**
     * Set the game to the end game phase
     * @param endGamePhase boolean true or false
     */
    public void setEndGamePhase(boolean endGamePhase);

    /**
     * @return Chat the chat
     */
    public Chat getChat();

}
