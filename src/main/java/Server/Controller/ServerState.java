package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.Player.Player;

import java.util.List;
import java.util.Map;

/**
 * This interface is used to handle the state of the server. Each class implementing this interface will handle a different state of the server
 */
public interface ServerState {
    /**
     * Adds a player to the game
     * @param name the player name
     * @param clientID the id of the client who's playing as the player
     * @throws TooManyPlayersException if the player limit has already been reached
     * @throws IllegalArgumentException if the name is already taken
     * @throws AlreadyStartedException if the game has already started
     */
    void addPlayer(String name, String clientID) throws AlreadyStartedException, TooManyPlayersException, IllegalArgumentException;
    /**
     * Connects a player to a loaded game
     * @param clientId the id of the client who is connecting
     * @param name the name of the player
     * @throws AlreadyStartedException if the game has already started
     * @throws IllegalArgumentException if the player is already connected
     * @throws PlayerNotFoundByNameException if there is no player with that name
     */
    void addSavedPlayer(String clientId, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException;
    /**
     * removes a player from the list of players
     * @param player the player
     */
    void removePlayer(Player player);
    /**
     * play a card from the hand to the manuscript
     * @param player the player
     * @param position the card position in the hand
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     * @param face the face choosen by the player
     * @throws TooFewElementsException if the player has already placed a card during the turn
     */
    void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace face) throws TooFewElementsException;
    /**
     * draw a card from one of the decks
     * @param player the player
     * @param deckPosition where i want to draw the card from
     * @param deck the deck
     * @throws TooManyElementsException if the player hasn't yet placed a card during the turn
     * @throws AlreadyFinishedException if the deck has no cards
     */
    void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, AlreadyFinishedException;
    /**
     * Reacts to the disconnection of a player, handling its possible removal from the game
     * @param id the id of the player
     */
    void reactToDisconnection(String id);
    /**
     * Reconnects a player to the game
     * @param newId the id of the client who is reconnecting
     * @param oldId the id of the player the client is reconnecting as
     * @throws NotYetStartedException if the game has not yet started
     * @throws AlreadyFinishedException if the game is already finished
     */
    void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) throws AlreadyFinishedException, NotYetStartedException;

    /**
     * Checks if the server is in the saved game lobby
     * @return true if the server is in the saved game lobby
     */
    boolean isInSavedGameLobby();
}
