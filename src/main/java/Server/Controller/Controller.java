package Server.Controller;

import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Enums.Color;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.IOException;
import java.util.List;

public interface Controller {

    public void addPlayer(String name, String clientID) throws TooManyPlayersException, IllegalArgumentException, AlreadyStartedException;


    /**
     * removes a player from the list of players
     * @param player the player
     */
    public void removePlayer(Player player) throws IllegalArgumentException;
    /**
     * @return List<Player> the list of players
     */
    public List<Player> getPlayerList();

    public void setOffline(String id);


    /**
     * set the player color
     * @param color the color
     * @param player the player
     */
    public void setPlayerColor(Color color, Player player) throws IllegalArgumentException, AlreadyStartedException;
    /**
     * sets a player ready status to ready
     * @param player the player
     */
    public void setReady(Player player) throws MissingInfoException, AlreadyStartedException;
    /**
     * sets a player status to not ready
     * @param player the player
     */
    public void setNotReady(Player player) throws MissingInfoException, AlreadyStartedException;
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
    public void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, MissingInfoException, AlreadyStartedException;
    /**
     * set starting card
     * @param player the player
     * @param face the face
     */
    public void setStartingCard(Player player, Face face) throws AlreadySetException, AlreadyStartedException, MissingInfoException;

    //GAME FLOW METHODS
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
    public void drawCard(Player player, DeckPosition deckposition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException;

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

    public void reactToDisconnection(String id) throws AlreadyFinishedException, PlayerNotFoundByNameException;

    public void reconnect(String newId, String oldId) throws IllegalArgumentException, AlreadySetException, NotYetStartedException, AlreadyFinishedException;

    public void addSavedPlayer(String id, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException;
}

