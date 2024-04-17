package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.StartingCard;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Manuscript.Manuscript;
import Server.Player.Player;

import java.util.List;

public interface Controller {
    /**
     * adds a player to the list of players
     * @param player the player
     */
    public void addPlayer(Player player);

    /**
     * removes a player from the list of players
     * @param player the player
     */
    public void removePlayer(Player player);
    /**
     * @return List<Player> the list of players
     */
    public List<Player> getPlayerList();
    /**
     * shuffle the player list
     */
    public void shufflePlayerList();
    /**
     * initialize the game
     */
    public void start();

    /**
     * set the player color
     * @param color the color
     * @param player the player
     */
    public void setPlayerColor(Color color, Player player);

    /**
     * gives 2 achievement cards to all players
     */
    public void giveSecretObjectiveCards();

    /**
     * sets a player's secret achievement card
     * @param player the player
     * @param card the card
     */
    public void setSecretObjectiveCard(Player player, AchievementCard card);
    /**
     * give starting cards to all players
     */
    public void giveStartingCards();
    /**
     * set starting card
     * @param player the player
     * @param card the card
     * @param face the face
     */
    public void setStartingCard(Player player, StartingCard card, Face face);
    /**
     * gives all players their initial hand
     */
    public void giveInitialHand();
    /**
     * goes to next turn
     */
    public void nextTurn();
    /**
     * @return int the current turn
     */
    public int getTurn();
    /**
     * checks the online status of a player
     * @param player the player
     * @return boolean true if the player is online
     */
    public boolean isOnline(Player player);
    /**
     * play a card from the hand to the manuscript
     * @param player the player
     * @param card the card
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     * @param face the face choosen by the player
     */
    public void playCard(Player player, Card card, int xCoord, int yCoord, Face face);

}

