package Server.GameModel;

import Server.Card.StartingCard;
import Server.Chat.Chat;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Exception.AlreadySetException;
import Server.Player.Player;

import java.util.List;

public interface GameModel {
    /**
     * Adds 1 to turn
     */
    void nextTurn();

    /**
     * @return ResourceDeck the resource deck
     */
    ResourceDeck getResourceDeck();

    /**
     * @return GoldDeck the gold deck
     */
    GoldDeck getGoldDeck();

    /**
     * @return AchievementDeck the achievement deck
     */
    AchievementDeck getAchievementDeck();

    /**
     * @return List<StartingCard> the starting cards
     */
    List<StartingCard> getStartingCards();

    /**
     * @return int the turn
     */
    int getTurn();

    /**
     * @return boolean true if the game is in the end game phase
     */
    boolean isEndGamePhase();

    /**
     * Set the game to the end game phase
     *
     */
    void setEndGamePhase() throws AlreadySetException;

    /**
     * @return Chat the chat
     */
    Chat getChat();
    /**
     * adds a player to the list of players
     * @param player the player
     */
    void addPlayer(Player player) throws IllegalArgumentException;
   /**
     * removes a player from the list of players
     * @param player the player
     */
   void removePlayer(Player player) throws IllegalArgumentException;

    /**
     * @return List<Player> the list of players
     */
    List<Player> getPlayerList();
    /**
     * sets the player list
     * @param playerList the player list
     */
    void setPlayerList(List<Player> playerList);
    /**
     * shuffles the player list
     */
    void shufflePlayerList();

    /**
     * Creates gold and resource decks
     */
    void createGoldResourceDecks() throws AlreadySetException;

    /**
     * Creates starting cards
     */
    void createStartingCards() throws AlreadySetException;

    /**
     * Creates achievement deck
     */
    void createAchievementDeck() throws AlreadySetException;

    int getActivePlayerIndex();

    boolean isLastRound();

    void setLastRound(boolean lastRound);
    void setActivePlayerIndex(int activePlayerIndex);

}
