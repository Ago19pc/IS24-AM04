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
     *
     */
    public void setEndGamePhase() throws AlreadySetException;

    /**
     * @return Chat the chat
     */
    public Chat getChat();
    /**
     * adds a player to the list of players
     * @param player the player
     */
    public void addPlayer(Player player) throws IllegalArgumentException;
   /**
     * removes a player from the list of players
     * @param player the player
     */
    public void removePlayer(Player player) throws IllegalArgumentException;

    /**
     * @return List<Player> the list of players
     */
    public List<Player> getPlayerList();
    /**
     * shuffles the player list
     * @return void
     */
    public void shufflePlayerList();

}
