package Client.View;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Chat.Message;
import Server.Enums.Actions;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * This interface is responsible for the user interface logic of the client side of the application.
 */
public interface UI {
    /**
     * Asks the user to choose the connection mode.
     */
    void askConnectionMode();

    /**
     * Displays the name change of the player.
     */
    void nameChanged();

    /**
     * Displays the color change of the player.
     */
    void colorChanged();
    /**
     * Informs the user that the connection has been set successfully
     */
    void successfulConnection();

    /**
     * Displays the new card in the hand of the player.
     */
    void displayNewCardInHand();

    /**
     * Informs the user that the name change of his player has failed.
     */
    void nameChangeFailed();

    /**
     * Informs the user that he needs to choose a name.
     */
    void needName();

    /**
     * Informs the user that he needs to choose a color.
     */
    void needColor();

    /**
     * Informs the user that he needs to choose a name or a color.
     */
    void needNameOrColor();

    /**
     * Informs the user that the color he has chosen is not available.
     */
    void unavailableColor();

    /**
     * Informs the user that the color change of his player has failed.
     */
    void colorChangeFailed();

    /**
     * Informs the user that the name he has chosen is not valid.
     */
    void invalidColor();

    /**
     * Informs the user that he needs to connect to a server.
     */
    void needConnection();

    /**
     * Informs the user that the connection to the server has failed.
     */
    void connectionFailed();

    /**
     * Informs the user that he cannot draw achievement cards.
     */
    void cantDrawAchievementCards();

    /**
     * Informs the user that he has already done an action
     * @param action the action that has already been done
     */
    void alreadyDone(Actions action);

    /**
     * Informs the user that the placement of a card has failed
     */
    void cardNotPlaceable();

    /**
     * Informs the user that he cannot send an empty chat message
     */
    void chatMessageIsEmpty();

    /**
     * Informs the user that there is no card to draw in the position he has chosen
     */
    void deckIsEmpty();

    /**
     * Informs the user that the game has already started
     */
    void gameAlreadyStarted();

    /**
     * Informs the user that the game has not yet started
     */
    void gameNotYetStarted();

    /**
     * Informs the user that the card he has chosen is not valid for the action he wants to do
     * @param cardType the action the user wants to do
     */
    void invalidCardForAction(Actions cardType);

    /**
     * Informs the user that he has not yet given a card for the action he wants to do
     * @param cardType the action the user wants to do
     */
    void notYetGivenCard(Actions cardType);

    /**
     * Informs the user that it's not his turn
     */
    void notYourTurn();

    /**
     * Informs the user that the list of players has changed
     */
    void playerListChanged();

    /**
     * Displays the colors of the players
     */
    void displayPlayerColors();

    /**
     * Displays a player being ready or not
     * @param name the name of the player
     * @param ready whether the player is ready or not
     */
    void updateReady(String name, boolean ready);

    /**
     * Displays a chat message
     * @param message the message to display
     */
    void chat(Message message);

    /**
     * Asks the user to choose a secret achievement
     * @param possibleAchievements the possible achievements to choose from
     */
    void chooseSecretAchievement(List<AchievementCard> possibleAchievements);

    /**
     * Displays end game phase starting
     */
    void endGameStarted();

    /**
     * Displays the hand of the player
     */
    void displayHand();

    /**
     * Displays the leaderboard
     * @param playerPoints the points of the players in the order of winning
     */
    void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints);

    /**
     * Displays a new player joining the game
     */
    void displayNewPlayer();

    /**
     * Displays a player drawing a card
     * @param name the name of the player
     * @param deckFrom the deck the player is drawing from
     * @param position the position in the deck the player is drawing from
     */
    void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position);

    /**
     * Displays the next turn
     */
    void newTurn();

    /**
     * Informs the user that a player has received their hand
     * @param name the name of the player
     */
    void otherPlayerInitialHand(String name);

    /**
     * Informs the user that a player has chosen a secret achievement
     * @param name the name of the player
     */
    void secretAchievementChosen(String name);

    /**
     * Informs the user that a player has chosen a starting card
     * @param name the name of the player
     */
    void startingCardChosen(String name);

    /**
     * Informs the user that all the players have joined
     */
    void gameStarted();

    /**
     * Displays the player order
     */
    void displayPlayerOrder();

    /**
     * Displays the board cards
     */
    void displayBoardCards();

    /**
     * Asks the user to choose the face of their starting card
     * @param card the card to choose the face of
     */
    void chooseStartingCardFace(Card card);

    /**
     * Inform the user that, in order to do the action, they need to do another action first
     * @param action the action that needs to be done first
     */
    void doFirst(Actions action);

    /**
     * Displays the game lobby
     */
    void displayLobby();

    /**
     * Displays the points of a player
     * @param playerName the name of the player
     * @throws PlayerNotFoundByNameException if there is no player with the given name
     */
    void displayPlayerPoints(String playerName) throws PlayerNotFoundByNameException;

    /**
     * Displays the placement of a card
     * @param playerName the name of the player who placed the card
     * @param cornerCardFace the face of the card that was placed
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     */
    void cardPlaced(String playerName, CornerCardFace cornerCardFace, int x, int y);

    /**
     * Displays the disconnection of a player
     * @param playerName the name of the player who disconnected
     */
    void playerDisconnected(String playerName);

    /**
     * Displays the common achievements
     */
    void displayCommonAchievements();

    /**
     * Inform the user that the match is full
     */
    void tooManyPlayers();

    /**
     * Displays the id of the player
     */
    void displayId();

    /**
     * Displays the remotion of a player
     * @param name the name of the player who was removed
     */
    void playerRemoved(String name);

    /**
     * Displays the reconnection of a player
     * @param name the name of the player who reconnected
     */
    void otherPlayerReconnected(String name);

    /**
     * Informs the user that the id he tried to connect as is not in the game
     */
    void idNotInGame();
    /**
     * Informs the user that the player he's trying to connect as is already playing
     */
    void playerAlreadyPlaying();

    /**
     * Displays the game info
     */
    void displayGameInfo();

    /**
     * Informs the user that the game has already finished
     */
    void gameAlreadyFinished();

    /**
     * Informs the user that the server has disconnected
     */
    void clear();
}