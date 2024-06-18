package Server.Controller;

import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Enums.*;
import Server.Exception.*;
import Server.Player.Player;

import java.io.IOException;
import java.util.List;

public interface Controller {

    void changeState(ServerState state);

    void addPlayer(String name, String clientID) throws TooManyPlayersException, IllegalArgumentException, AlreadyStartedException;


    /**
     * removes a player from the list of players
     * @param player the player
     */
    void removePlayer(Player player) throws IllegalArgumentException;
    /**
     * @return List<Player> the list of players
     */
    List<Player> getPlayerList();

    void setOffline(String id);

    /**
     * set the player color
     * @param color the color
     * @param player the player
     */
    void setPlayerColor(Color color, Player player) throws IllegalArgumentException, AlreadyStartedException;
    /**
     * sets a player ready status to ready
     * @param player the player
     */
    void setReady(Player player) throws MissingInfoException, AlreadyStartedException;
    /**
     * sets a player status to not ready
     * @param player the player
     */
    void setNotReady(Player player) throws MissingInfoException, AlreadyStartedException;
    /**
     * returns a player's ready status
     * @param player the player
     * @return boolean true if the player is ready
     */
    boolean isReady(Player player);
    /**
     * sets a player's secret achievement card
     * @param player the player
     * @param cardNumber the card number between the 2 given (0 or 1)
     */
    void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, MissingInfoException, AlreadyStartedException;
    /**
     * set starting card
     * @param player the player
     * @param face the face
     */
    void setStartingCard(Player player, Face face) throws AlreadySetException, AlreadyStartedException, MissingInfoException;

    //GAME FLOW METHODS
    /**
     * @return int the current turn
     */
    int getTurn();
    /**
     * checks the online status of a player
     * @param player the player
     * @return boolean true if the player is online
     */
    boolean isOnline(Player player);

    //PLAYER ACTIONS
    /**
     * play a card from the hand to the manuscript
     * @param player the player
     * @param position the card position in the hand
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     * @param face the face choosen by the player
     */
    void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException;

    /**
     * draw a card from one of the decks
     * @param player the player
     * @param deckposition where i want to draw the card from
     * @param deck the deck
     */
    void drawCard(Player player, DeckPosition deckposition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException;

    //CHAT METHODS
    /**
     * adds a message to the chat
     * @param message the message string
     * @param player the player who sent the message
     */
    void addMessage(String message, Player player) throws IllegalArgumentException;

    /**
     * @return List<Message> the chat messages
     */
    List<Message> getChatMessages();

    //SAVED GAMES METHODS
    /**
     * saves a game to a json file
     */
    void saveGame() throws IOException;

    /**
     * loads a game from a json file
     */
    void loadGame() throws IOException;

    /**
     * @param name the name of the player
     * @return the Player instance associated with the name
     * @throws PlayerNotFoundByNameException if the player cannot be found
     */
    Player getPlayerByName(String name) throws PlayerNotFoundByNameException;

    /**
     * @return the ServerConnectionHandler
     */

    GeneralServerConnectionHandler getConnectionHandler();


    /**
     * Prints some data
     */
    void printData();

    void reactToDisconnection(String id) throws AlreadyFinishedException, PlayerNotFoundByNameException;

    void reconnect(String newId, String oldId) throws IllegalArgumentException, AlreadySetException, NotYetStartedException, AlreadyFinishedException;

    void addSavedPlayer(String id, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException;

    void shufflePlayerList();

    void nextTurn();

    void giveInitialHand() throws AlreadySetException, AlreadyFinishedException;

    void computeLeaderboard() throws AlreadyFinishedException;

    void endGame() throws AlreadySetException;

    void start() throws TooFewElementsException, AlreadySetException;

    boolean isInSavedGameLobby();
}

