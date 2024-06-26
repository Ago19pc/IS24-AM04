package Client.Controller;

import Client.Deck;
import Client.Player;
import Client.View.UI;
import Server.Card.*;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This interface is the controller for the client side of the application.
 * It manages the game state on the client side, with its implementation holding the data displayed by the UI.
 * It responds to UI actions, sends messages to the server and updates the game state based on incoming messages.
 */
public interface ClientController {
    /**
     * Initializes the controller
     * @param ui the ui to assign to the controller
     * @throws RemoteException if the controller can't be initialized
     */
    void main(UI ui) throws RemoteException;
    /**
     * Returns whether a saved game is playing
     * @return true if the game being played was loaded from a previous save, false otherwise
     */
    boolean isSavedGame();
    /**
     * Sets the User's players name to the given name
     * @param name the name to set
     */
    void setName(String name);
    /**
     * Gets the User's two starting possible secret achievements from which he has to choose one
     * @return the two possible secret achievements in a list
     */
    List<AchievementCard> getPotentialSecretAchievements();
    /**
     * Selects one of the cards in the player's hand
     * @param chosenHandCard the card to select: 0 for the first card, 1 for the second card, etc.
     */
    void setChosenHandCard(Integer chosenHandCard);
    /**
     * Gets the index of the card the player has chosen from his hand
     * @return the index of the chosen card: 0 if the first card was chosen, 1 if the second card was chosen, etc.
     */
    Integer getChosenHandCard();
    /**
     * Gets a player object by its name
     * @param name the name of the player to get
     * @return the player object with the given name
     * @throws PlayerNotFoundByNameException if no player with the given name is found
     */
    Player getPlayerByName(String name) throws PlayerNotFoundByNameException;
    /**
     * Gets the User's player's color
     * @return the color of the User's player. If the color has not been set yet, returns null
     */
    Color getMyColor();
    /**
     * Gets the User's id. This is used for reconnecting to a game
     * @return the id of the user
     */
    String getMyId();
    /**
     * Sets the User's player's color to the given color
     * @param color the color to set
     */
    void setMyColor(Color color);
    /**
     * Sets the User's id to the given id
     * @param id the id to set
     */
    void setId(String id);
    /**
     * Reconnects to a game with the given id
     * @param newId the id required by the server to reconnect to the game
     */
    void reconnect(String newId);
    /**
     * Joins a saved game
     * @param name the name of the player to join as
     */
    void joinSavedGame(String name);
    /**
     * Joins a server with the given ip and port
     * @param ip the ip of the server
     * @param port the port of the server
     */
    void joinServer(String ip, int port);
    /**
     * Sends a message to the server setting the player's ready status to be true
     */
    void setReady();
    /**
     * Sends a message to the server setting the player's color to the given color
     * @param color the color to set
     */
    void askSetColor(Color color);
    /**
     * Sends a message to the chat
     * @param message the text of the message to send
     */
    void sendChatMessage(String message);
    /**
     * Sends a message to the server asking to create a new player with the given name to play as
     * @param name the name of the player to play as
     */
    void askSetName(String name);
    /**
     * Sets the RMI mode of the client: true if the client is in RMI mode, false if it is in Socket mode.
     * After setting the mode, the client will create the connection handler for the selected mode
     * @param rmi the mode to set
     */
    void setRMIMode(boolean rmi);
    /**
     * Sends a message to the server selecting the face of the starting card
     * @param face the face to select: FRONT or BACK
     */
    void chooseStartingCardFace(Face face);
    /**
     * Sends a message to the server selecting one of the possible secret achievements
     * @param index the index of the secret achievement to select: 0 for the first secret achievement, 1 for the second secret achievement
     */
    void chooseSecretAchievement(int index);
    /**
     * Sends a message to the server asking to place a card
     * @param cardNumber the index of the card to play in the player's hand
     * @param face the face of the card to play: FRONT or BACK
     * @param x the x coordinate of the position where the card should be placed
     * @param y the y coordinate of the position where the card should be placed
     */
    void askPlayCard(int cardNumber, Face face, int x, int y);
    /**
     * Sends a message to the server asking to draw a card
     * @param deck the deck to draw from: GOLD or RESOURCE
     * @param deckPosition the position to draw from: DECK to draw from the deck itself, FIRST_CARD to draw from the first card on the board or SECOND_CARD to draw from the second card on the board
     */
    void askDrawCard(Decks deck, DeckPosition deckPosition);

    /**
     * Gets the User's player's name
     * @return the name of the User's player
     */
    String getMyName();
    /**
     * Gets the players in the game
     * @return a list of the players in the game
     */
    List<Player> getPlayers();
    /**
     * Gets the current game state
     * @return the current game state
     */
    GameState getGameState();
    /**
     * Gets the available colors
     * @return list of available colors
     */
    List<Color> getAvailableColors();
    /**
     * Gets the player's hand
     * @return the player's hand as a list of Card
     */
    List<Card> getHand();
    /**
     * Gets the common achievements
     * @return the common achievements as a list of achievementCard
     */
    List<AchievementCard> getCommonAchievements();
    /**
     * Gets the leaderboard
     * @return the leaderboard as a map of player names to points
     */
    Map<String, Integer> getLeaderboard();
    /**
     * Gets a deck's size i.e. the number of cards left in the deck
     * @param deck the deck to get the size of: GOLD or RESOURCE
     * @return the size of the deck
     */
    int getDeckSize(Decks deck);
    /**
     * Gets the board cards of a deck
     * @param deck the deck to get the board cards of: GOLD or RESOURCE
     * @return the board cards of the deck in a list of Card
     */
    List<Card> getBoardCards(Decks deck);
    /**
     * Gets the player who is currently active i.e. whose turn it is
     * @return the active player | null if no player is active
     */
    Player getActivePlayer();
    /**
     * gets the current turn number
     * @return the current turn number
     */
    int getTurn();
    /**
     * Gets the names of the players in the game
     * @return a list of the names of the players in the game
     */
    List<String> getPlayerNames();
    /**
     * Loads the data needed while in the lobby. This is called by a LobbyPlayersMessage when the client connects successfully to a server
     * @param id the client id needed to reconnect to the game
     * @param playerNames a list of the names of the players in the game
     * @param playerColors a map of the players' names to their colors
     * @param playerReady a map of the players' names to their ready status
     * @param isSavedGame whether the game is a saved game or a new game
     */
    void loadLobbyInfo(String id, List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, Boolean isSavedGame);
    /**
     * Sets the common and possible secret achievement cards
     * @param secretCards a list of the possible secret achievement cards to choose from
     * @param commonCards a list of the common achievement cards
     */
    void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards);
    /**
     * Displays that the player cannot draw from the achievement deck
     */
    void achievementDeckDrawInvalid();
    /**
     * Displays that the action the player is trying to do has already been done
     * @param action the action that has already been done
     */
    void alreadyDone(Actions action);
    /**
     * Displays that a card is not placeable
     */
    void cardNotPlaceable();
    /**
     * Appends a message to the chat
     * @param message the message to add
     */
    void addChatMessage(Message message);
    /**
     * Displays that the user cannot send an empty chat message
     */
    void chatMessageIsEmpty();
    /**
     * Displays that the user cannot do what they are trying to do without setting their color first
     */
    void colorNotYetSet();
    /**
     * Adds a card to the player's hand
     * @param drawnCard the card to add
     */
    void giveDrawnCard(Card drawnCard);
    /**
     * Displays that a deck is empty
     */
    void emptyDeck();
    /**
     * Displays that the game is in the end phase
     */
    void setEndGame();
    /**
     * Displays that the game has already started so the player cannot do what they are trying to do
     */
    void gameAlreadyStarted();
    /**
     * Displays that the game has not yet started so the player cannot do what they are trying to do
     */
    void gameNotYetStarted();
    /**
     * Gives the player their initial hand
     * @param hand the initial hand to give
     */
    void giveInitialHand(List<Card> hand);
    /**
     * Displays that the card selected is invalid for a certain action
     * @param cardType the action the card is invalid for
     */
    void invalidCard(Actions cardType);
    /**
     * Displays the leaderboard
     * @param playerPoints a map of the player names to their points, in the order of the leaderboard
     */
    void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints);
    /**
     * Displays thet the player cannot do what they are trying to do without setting their name first
     */
    void nameNotYetSet();
    /**
     * Add a new player to the game
     * @param playerNames the new list of player names
     */
    void newPlayer(List<String> playerNames);
    /**
     * Displays that the player doesn't have a card yet to do a certain action
     * @param type the action the player doesn't have a card for
     */
    void notYetGivenCard(Actions type);
    /**
     * Displays that it's not the player's turn
     */
    void notYourTurn();
    /**
     * Handles the start of a new turn
     * @param activePlayerName the name of the player whose turn it is
     * @param turnNumber the number of the turn
     */
    void newTurn(String activePlayerName, int turnNumber);
    /**
     * Handles another player drawing a card
     * @param name the name of the player who drew the card
     * @param deckFrom the deck the card was drawn from: GOLD or RESOURCE
     * @param drawPosition the position the card was drawn from: DECK, FIRST_CARD or SECOND_CARD
     * @param newBoardCards the new board cards of the corrisponding deck after the card was drawn
     */
    void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards);
    /**
     * Handles another player receiving their initial hand
     * @param name the name of the player who received their initial hand
     */
    void giveOtherPlayerInitialHand(String name);
    /**
     * Sets the player's color
     * @param confirmation whether the color was set successfully
     * @param color the color that was set
     */
    void setColor(boolean confirmation, Color color);
    /**
     * Updates the player with the new color and tells the user which players have which colors
     * @param color the new color
     * @param name the name of the player
     */
    void updatePlayerColors(Color color, String name);
    /**
     * Sets the player's name to the proposed name if the name was set successfully
     * @param confirmation whether the name was set successfully
     */
    void setName(Boolean confirmation);
    /**
     * Updates the player with the new ready status
     * @param ready new ready status
     * @param name name of the player
     */
    void updatePlayerReady(boolean ready, String name);
    /**
     * Handles a secret card choice, which can be done by the player themselves or by another player
     * @param name the name of the player who chose the secret card
     */
    void setSecretCard(String name);
    /**
     * Sets the secret card for the player
     * @param chosenCard the index of the secret card to set
     */
    void setSecretCard(int chosenCard);
    /**
     * Handles the starting card choice, which can be done by the player themselves or by another player
     * @param name the name of the player who chose the starting card
     * @param startingFace the face selected
     */
    void startingCardChosen(String name, CornerCardFace startingFace);
    /**
     * Sets the board cards and displays the game start
     * @param goldBoardCards the gold cards on the board
     * @param resourceBoardCards the resource cards on the board
     */
    void startGame(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards);
    /**
     * Updates the order of the players list
     * @param playerNames the new order of the players as a list of names
     */
    void updatePlayerOrder(List<String> playerNames);
    /**
     * Handles the starting card being given to the player
     * @param card the card given to the player
     */
    void giveStartingCard(Card card);
    /**
     * Displays that the player cannot do what they are trying to do without doing something else first
     * @param actionToDo the action the player needs to do first
     */
    void toDoFirst(Actions actionToDo);
    /**
     * Handles the placement of a card on the board by a player
     * @param playerName the name of the player who placed the card
     * @param placedCardFace the face placed
     * @param x the x coordinate of the position where the card was placed
     * @param y the y coordinate of the position where the card was placed
     * @param points the points the player received for placing the card
     */
    void placeCard(String playerName, CornerCardFace placedCardFace, int x, int y, int points);
    /**
     * Displays a player's disconnection
     * @param playerName the name of the player who disconnected
     */
    void playerDisconnected(String playerName);
    /**
     * Displays that the chosen name is invalid
     */
    void invalidName();
    /**
     * Displays that there are too many players in the game
     */
    void tooManyPlayers();
    /**
     * Handles the remotion of a player from the game
     * @param name the name of the player who was removed
     */
    void playerRemoved(String name);
    /**
     * Displays the reconnection of a player
     * @param name the name of the player who reconnected
     */
    void otherPlayerReconnected(String name);
    /**
     * Displays that the chosen id is not in the game
     */
    void idNotInGame();
    /**
     * Displays that the player is already playing
     */
    void playerAlreadyPlaying();
    /**
     * Gets the secret achievement card
     * @return the secret achievement card
     */
    AchievementCard getSecretAchievement();
    /**
     * Gets all the chat
     * @return all the messages in the chat as a list of Message
     */
    List<Message> getChat();
    /**
     * Sets the data for a game. Called after reconnecting to a game
     * @param id the id of the player
     * @param commonAchievements the common achievements
     * @param goldDeck the gold deck
     * @param resourceDeck the resource deck
     * @param name the name of the player
     * @param secretAchievement the secret achievement card
     * @param hand the player's hand
     * @param turn the current turn number
     * @param players the players in the game
     * @param chat the chat
     * @param gameState the current game state
     */
    void setGameInfo(String id, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, String name, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState);
    /**
     * Loads game data. Called after a saved game has started
     * @param commonAchievements the common achievements
     * @param goldDeck the gold deck
     * @param resourceDeck the resource deck
     * @param secretAchievement the secret achievement card
     * @param hand the player's hand
     * @param turn the current turn number
     * @param players the players in the game
     * @param chat the chat
     * @param gameState the current game state
     * @param name the name of the player
     */
    void loadGame(List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState, String name);
    /**
     * Displays that the game has already finished
     */
    void gameAlreadyFinished();

    /**
     * Gets the index of the secret achievement card the player has chosen
     * @return the index
     */
    int getIndexofSecretAchievement();

    /**
     * Reacts to server disconnection
     */
    void serverDisconnected();
    /**
     * Clears the controller, preparing it for a new game
     */
    void clear();
    /**
     * Clears the UI, preparing it for a new game
     */
    void clearUI();
    /**
     * Sets the game state to the one passed in the arguments
     * @param gameState the new game state
     */
    void setGameState(GameState gameState);
}
