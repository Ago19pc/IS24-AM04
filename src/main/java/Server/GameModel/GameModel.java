package Server.GameModel;

import Server.Card.StartingCard;
import Server.Chat.Chat;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Exception.AlreadySetException;
import Server.Player.Player;

import java.util.List;

/**
 * Interface for the game model. This has basic actions that are used by the controller
 */
public interface GameModel {
    /**
     * Adds 1 to turn
     */
    void nextTurn();

    /**
     * Gets the resource deck
     * @return the resource deck
     */
    ResourceDeck getResourceDeck();

    /**
     * Gets the gold deck
     * @return the gold deck
     */
    GoldDeck getGoldDeck();

    /**
     * Gets the achievement deck
     * @return the achievement deck
     */
    AchievementDeck getAchievementDeck();

    /**
     * Gets the list of starting cards
     * @return the starting cards
     */
    List<StartingCard> getStartingCards();

    /**
     * Gets the current turn
     * @return the turn
     */
    int getTurn();

    /**
     * Checks if the game is in the end game phase
     * @return true if the game is in the end game phase
     */
    boolean isEndGamePhase();

    /**
     * Set the game to the end game phase
     * @throws AlreadySetException if the game is already in the end game phase
     */
    void setEndGamePhase() throws AlreadySetException;

    /**
     * Gets the chat
     * @return Chat the chat
     */
    Chat getChat();
    /**
     * adds a player to the list of players
     * @param player the player
     * @throws IllegalArgumentException if there already is a player with the same name
     */
    void addPlayer(Player player) throws IllegalArgumentException;
   /**
     * removes a player from the list of players
     * @param player the player
     * @throws IllegalArgumentException if the player is not in the list
     */
   void removePlayer(Player player) throws IllegalArgumentException;

    /**
     * Gets the list of all players
     * @return the list of players
     */
    List<Player> getPlayerList();
    /**
     * sets the player list
     * @param playerList the new player list
     */
    void setPlayerList(List<Player> playerList);
    /**
     * shuffles the player list
     */
    void shufflePlayerList();

    /**
     * Creates gold and resource decks
     * @throws AlreadySetException if the decks are already created
     */
    void createGoldResourceDecks() throws AlreadySetException;

    /**
     * Creates starting cards
     * @throws AlreadySetException if the starting cards are already created
     */
    void createStartingCards() throws AlreadySetException;

    /**
     * Creates achievement deck
     * @throws AlreadySetException if the achievement deck is already created
     */
    void createAchievementDeck() throws AlreadySetException;

    /**
     * Gets the index of the active player
     * @return the index of the active player
     */
    int getActivePlayerIndex();

    /**
     * Checks if the game is in the last round
     * @return true if the game is in the last round
     */
    boolean isLastRound();

    /**
     * Sets the game to the last round
     * @param lastRound true to set the game to the last round
     */
    void setLastRound(boolean lastRound);

    /**
     * Sets the index of the active player
     * @param activePlayerIndex the index to set
     */
    void setActivePlayerIndex(int activePlayerIndex);

}
