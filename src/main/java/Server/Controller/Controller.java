package Server.Controller;

import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Enums.*;
import Server.Exception.*;
import Server.Player.Player;

import java.io.IOException;
import java.util.List;

/**
 * This interface is used to represent the controller of the server. It manages the game flow and the game model.
 */
public interface Controller {
    /**
     * Changes the state of the controller
     * @param state the new state
     */
    void changeState(ServerState state);

    /**
     * Adds a player to the game
     * @param name the player name
     * @param clientID the id of the client who's playing as the player
     * @throws TooManyPlayersException if the player limit has already been reached
     * @throws IllegalArgumentException if the name is already taken
     * @throws AlreadyStartedException if the game has already started
     */
    void addPlayer(String name, String clientID) throws TooManyPlayersException, IllegalArgumentException, AlreadyStartedException;


    /**
     * removes a player from the list of players
     * @param player the player
     * @throws IllegalArgumentException if the player is not in the list
     */
    void removePlayer(Player player) throws IllegalArgumentException;
    /**
     * returns the list of players in the game
     * @return the list of players
     */
    List<Player> getPlayerList();

    /**
     * Reacts to the disconnection of a client, setting the player offline.
     * @param id the id of the client
     */
    void setOffline(String id);

    /**
     * set the player color
     * @param color the color
     * @param player the player
     * @throws IllegalArgumentException if the color is already taken
     * @throws AlreadyStartedException if the game has already started
     */
    void setPlayerColor(Color color, Player player) throws IllegalArgumentException, AlreadyStartedException;
    /**
     * sets a player ready status to ready
     * @param player the player
     * @throws MissingInfoException if the player does not have a color
     * @throws AlreadyStartedException if the game has already started
     */
    void setReady(Player player) throws MissingInfoException, AlreadyStartedException;
    /**
     * sets a player status to not ready
     * @param player the player
     * @throws MissingInfoException if the player does not have a color
     * @throws AlreadyStartedException if the game has already started
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
     * @throws AlreadySetException if the player has already set the secret achievement
     * @throws MissingInfoException if the player was not given the choice of secret achievement
     * @throws AlreadyStartedException if the game has already started
     */
    void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, MissingInfoException, AlreadyStartedException;
    /**
     * set starting card
     * @param player the player
     * @param face the face
     * @throws AlreadySetException if the player has already set the starting face
     * @throws AlreadyStartedException if the game has already started
     * @throws MissingInfoException if the player was not given the starting card
     */
    void setStartingCard(Player player, Face face) throws AlreadySetException, AlreadyStartedException, MissingInfoException;

    //GAME FLOW METHODS
    /**
     * returns the current turn number
     * @return the current turn
     */
    int getTurn();
    /**
     * checks the online status of a player
     * @param player the player
     * @return true if the player is online
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
     * @throws TooFewElementsException if the player has already placed a card during the turn
     * @throws InvalidMoveException if it's not the player's turn or the placement is not valid
     */
    void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException;

    /**
     * draw a card from one of the decks
     * @param player the player
     * @param deckposition where i want to draw the card from
     * @param deck the deck
     * @throws TooManyElementsException if the player hasn't yet placed a card during the turn
     * @throws InvalidMoveException if it's not the player's turn
     * @throws AlreadyFinishedException if the deck has no cards
     * @throws NotYetStartedException if the game has not yet started
     */
    void drawCard(Player player, DeckPosition deckposition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException;

    //CHAT METHODS
    /**
     * adds a message to the chat
     * @param message the message string
     * @param player the player who sent the message
     * @throws IllegalArgumentException if the message is empty
     */
    void addMessage(String message, Player player) throws IllegalArgumentException;

    //SAVED GAMES METHODS
    /**
     * saves a game to a json file
     * @throws IOException if the file can't be saved
     */
    void saveGame() throws IOException;

    /**
     * loads a game from a json file
     * @throws IOException if the file can't be loaded
     * @throws NullPointerException if the file is empty
     */
    void loadGame() throws IOException, NullPointerException;

    /**
     * Returns the player instance associated with the name
     * @param name the name of the player
     * @return the Player instance associated with the name
     * @throws PlayerNotFoundByNameException if there is no player with that name
     */
    Player getPlayerByName(String name) throws PlayerNotFoundByNameException;

    /**
     * returns the connection handler
     * @return the ServerConnectionHandler
     */
    GeneralServerConnectionHandler getConnectionHandler();


    /**
     * Reacts to the disconnection of a player, handling its possible removal from the game
     * @param id the id of the player
     * @throws AlreadyFinishedException if the game is already finished
     * @throws PlayerNotFoundByNameException if there is no player with that name
     */
    void reactToDisconnection(String id) throws AlreadyFinishedException, PlayerNotFoundByNameException;

    /**
     * Reconnects a player to the game
     * @param newId the id of the client who is reconnecting
     * @param oldId the id of the player the client is reconnecting as
     * @throws IllegalArgumentException if there is no player with that id
     * @throws AlreadySetException if the player with that id is already connected
     * @throws NotYetStartedException if the game has not yet started
     * @throws AlreadyFinishedException if the game is already finished
     */
    void reconnect(String newId, String oldId) throws IllegalArgumentException, AlreadySetException, NotYetStartedException, AlreadyFinishedException;

    /**
     * Connects a player to a loaded game
     * @param id the id of the client who is connecting
     * @param name the name of the player
     * @throws AlreadyStartedException if the game has already started
     * @throws IllegalArgumentException if the player is already connected
     * @throws PlayerNotFoundByNameException if there is no player with that name
     */
    void addSavedPlayer(String id, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException;
    /**
     * Shuffles the player list and sends the new order to all players
     */
    void shufflePlayerList();
    /**
     * Sets the next turn. Sets the end game phase or ends the game if necessary
     */
    void nextTurn();
    /**
     * Gives the initial hand to the players
     * @throws AlreadySetException if the initial hand is already given
     * @throws AlreadyFinishedException if the deck is empty
     */
    void giveInitialHand() throws AlreadySetException, AlreadyFinishedException;
    /**
     * Computes the leaderboard and sends it to all players
     * @throws AlreadyFinishedException if the leaderboard is already computed
     */
    void computeLeaderboard() throws AlreadyFinishedException;

    void disconnectionLeaderboard();
    /**
     * Starts the end game phase
     * @throws AlreadySetException if the end game phase is already set
     */
    void endGame() throws AlreadySetException;
    /**
     * Starts the game by creating the decks and giving the starting cards to the players
     * @throws TooFewElementsException if there are not enough players or not all players are ready
     * @throws AlreadySetException if the decks are already set
     */
    void start() throws TooFewElementsException, AlreadySetException;
    /**
     * Checks if the server is in the saved game lobby
     * @return true if the server is in the saved game lobby
     */
    boolean isInSavedGameLobby();

    /**
     * Clears the game model and prepares for a new game
     */
    void clear();
}

