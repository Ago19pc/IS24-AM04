package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.StartingCard;
import Server.Chat.Message;
import Server.Connections.ServerConnectionHandler;
import Server.Deck.Deckable;
import Server.Enums.Color;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.IOException;
import java.util.List;

public interface Controller {
    /**
     * adds a player to the list of players
     * @param player the player
     */
    public void addPlayer(Player player);

    public void addPlayer(String name);

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

    /**
     * draw a card from one of the decks
     * @param player the player
     * @param deckposition where i want to draw the card from
     * @param deck the deck
     */
    public void drawCard(Player player, DeckPosition deckposition, Decks deck);
    /**
     * check if a card can be placed
     * @param card the card
     * @param face the cardface
     * @return boolean true if tha card is playable
     */
    //public Boolean isPlayable(Card card, Face face);
    /**
     * sets the game to ending phase
     *
     */
    public void endGame();

    /**
     * compute achievement points and leaderboard
     * @return List<Player> the leaderboard
     */
    public List<Player> computeLeaderboard();

    /**
     * clears everything, preparing for a new game
     */
    public void clear();

    /**
     * sets a player ready status to ready
     * @param player the player
     */
    public void setReady(Player player);
    /**
     * sets a player status to not ready
     * @param player the player
     */
    public void setNotReady(Player player);
    /**
     * returns a player's ready status
     * @param player the player
     * @return boolean true if the player is ready
     */
    public boolean isReady(Player player);

    /**
     * adds a message to the chat
     * @param message the message string
     * @param player the player who sent the message
     */
    public void addMessage(String message, Player player);

    /**
     * @return List<Message> the chat messages
     */
    public List<Message> getChatMessages();

    /**
     * saves a game to a json file
     */
    public void saveGame() throws IOException;

    /**
     * loads a game from a json file
     */
    public void loadGame() throws IOException;

    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException;

    public ServerConnectionHandler getConnectionHandler();


}

