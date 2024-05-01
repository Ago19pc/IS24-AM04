package Server.Controller;

import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Connections.ServerConnectionHandler;
import Server.Connections.ServerConnectionHandlerRMI;
import Server.Connections.ServerConnectionHandlerSOCKET;
import Server.Enums.Color;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.IOException;
import java.util.List;

public interface Controller {

    public void addPlayer(String name) throws TooManyPlayersException;


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
     * shuffle the player list
     */
    public void shufflePlayerList();


    /**
     * set the player color
     * @param color the color
     * @param player the player
     */
    public void setPlayerColor(Color color, Player player) throws IllegalArgumentException;
    /**
     * sets a player ready status to ready
     * @param player the player
     */
    public void setReady(Player player) throws MissingInfoException;
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
     * sets a player's secret achievement card
     * @param player the player
     * @param cardNumber the card number between the 2 given (0 or 1)
     */
    public void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, AlreadyFinishedException, MissingInfoException;
    /**
     * set starting card
     * @param player the player
     * @param face the face
     */
    public void setStartingCard(Player player, Face face) throws AlreadySetException;

    //GAME INITIALIZATION METHODS
    /**
     * initialize the game
     */
    public void start() throws TooFewElementsException, AlreadySetException, TooFewElementsException;
    /**
     * gives 2 achievement cards to all players
     */
    public void giveSecretObjectiveCards();
    /**
     * give starting cards to all players
     */
    public void giveStartingCards();
    /**
     * gives all players their initial hand
     */
    public void giveInitialHand() throws AlreadySetException, AlreadyFinishedException;

    //GAME FLOW METHODS
    /**
     * goes to next turn
     */
    public void nextTurn() throws MissingInfoException, AlreadyFinishedException;
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
     * sets the game to ending phase
     *
     */
    public void endGame() throws AlreadySetException;
    /**
     * compute achievement points and leaderboard
     * @return List<Player> the leaderboard
     */
    public List<Player> computeLeaderboard() throws AlreadyFinishedException;

    /**
     * clears everything, preparing for a new game
     */
    public void clear();

    //PLAYER ACTIONS
    /**
     * play a card from the hand to the manuscript
     * @param player the player
     * @param position the card position in the hand
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     * @param face the face choosen by the player
     */
    public void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException;

    /**
     * draw a card from one of the decks
     * @param player the player
     * @param deckposition where i want to draw the card from
     * @param deck the deck
     */
    public void drawCard(Player player, DeckPosition deckposition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException;

    //CHAT METHODS
    /**
     * adds a message to the chat
     * @param message the message string
     * @param player the player who sent the message
     */
    public void addMessage(String message, Player player) throws IllegalArgumentException;

    /**
     * @return List<Message> the chat messages
     */
    public List<Message> getChatMessages();

    //SAVED GAMES METHODS
    /**
     * saves a game to a json file
     */
    public void saveGame() throws IOException;

    /**
     * loads a game from a json file
     */
    public void loadGame() throws IOException;

    /**
     * @param name
     * @return the Player instance associated with the name
     * @throws PlayerNotFoundByNameException
     */
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException;

    /**
     * @return the ServerConnectionHandler
     */

    public GeneralServerConnectionHandler getConnectionHandler();


    /**
     * Prints some data
     */
    public void printData();

    /**
     * Acknowledge the initialization of the hand, its uses a static variable to count up to the player count,
     * when this condition is met, the Achievement Cards are given to the players
     */
    public void ackInitHand();

    /**
     * Acknowledge the initialization of the board, its uses a static variable to count up to the player count,
     * when this condition is met, the Starting Cards are given to the players
     */
    public void ackInitBoard();

}

