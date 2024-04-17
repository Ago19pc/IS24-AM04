package Server.GameModel;

import Server.Card.StartingCard;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Chat.Chat;
import Server.Player.Player;

import java.util.List;

public interface GameModel {

    public void setPlayerList(List<Player> playerList);

    public List<Player> getPlayerList();

    public void addPlayer(Player player);


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
    public void setEndGamePhase();

    /**
     * @return Chat the chat
     */
    public Chat getChat();

}
