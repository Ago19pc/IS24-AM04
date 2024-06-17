package Client.Controller;

import Client.Connection.GeneralClientConnectionHandler;
import Client.Deck;
import Client.Player;
import Client.View.UI;
import Server.Card.*;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller for the client side of the application.
 * It manages the game state on the client side, holding the data displayed by the UI.
 * It responds to UI actions, sends messages to the server and updates the game state based on incoming messages.
 */
public class ClientController {
    private GeneralClientConnectionHandler clientConnectionHandler;
    private UI ui;
    private boolean rmiMode = false;

    //cards not owned by player info
    private List<AchievementCard> commonAchievements;
    private Deck<GoldCard> goldDeck;
    private Deck<ResourceCard> resourceDeck;

    //player info
    private String myName;
    private AchievementCard secretAchievement;
    private List<Card> hand = new ArrayList<>();

    private String id;

    //other game info
    private int turn = 0;
    private List<Color> unavaiableColors = new ArrayList<>();
    private Chat chat = new Chat();
    private List<Player> players = new ArrayList<>();
    private GameState gameState;

    //temp stuff
    private String proposedName;
    private Color proposedColor;
    private int indexofSecretAchievement;
    private List<AchievementCard> potentialSecretAchievements;
    private Integer chosenHandCard;
    private Boolean isSavedGame;

    /**
     * Intializes the controller
     * @param ui the ui to assign to the controller
     */
    public void main(UI ui) {
        this.ui = ui;
        this.gameState = GameState.LOBBY;
    }

    /**
     * Returns whether a saved game is playing
     * @return true if the game being played was loaded from a previous save, false otherwise
     */
    public boolean isSavedGame() {
        return isSavedGame;
    }

    /**
     * Sets the User's players name to the given name
     * @param name the name to set
     */
    public void setName(String name) {
        this.myName = name;
    }

    /**
     * Gets the User's two starting possible secret achievements from which he has to choose one
     * @return the two possible secret achievements in a list
     */
    public List<AchievementCard> getPotentialSecretAchievements() {
        return potentialSecretAchievements;
    }

    /**
     * Selects one of the cards in the player's hand
     * @param chosenHandCard the card to select: 0 for the first card, 1 for the second card, etc.
     */
    public void setChosenHandCard(Integer chosenHandCard) {
        this.chosenHandCard = chosenHandCard;
    }

    /**
     * Gets the index of the card the player has chosen from his hand
     * @return the index of the chosen card: 0 if the first card was chosen, 1 if the second card was chosen, etc.
     */
    public Integer getChosenHandCard() {
        return chosenHandCard;
    }

    //helper methods

    /**
     * Gets a player object by its name
     * @param name the name of the player to get
     * @return the player object with the given name
     * @throws PlayerNotFoundByNameException if no player with the given name is found
     */
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    /**
     * Gets the User's player's color
     * @return the color of the User's player. If the color has not been set yet, returns null
     */
    public Color getMyColor() {
        Color myColor = null;
        try {
            myColor = getPlayerByName(myName).getColor();
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
        return myColor;
    }

    /**
     * Gets the User's id. This is used for reconnecting to a game
     * @return the id of the user
     */
    public String getMyId() {
        return id;
    }
    /**
     * Sets the User's player's color to the given color
     * @param color the color to set
     */
    public void setMyColor(Color color) {
        try {
            getPlayerByName(myName).setColor(color);
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets the User's id to the given id
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    //ui actions
    /**
     * Reconnects to a game with the given id
     * @param newId the id required by the server to reconnect to the game
     */
    public void reconnect(String newId) {
        System.out.println("Reconnecting");
        ReconnectionMessage message = new ReconnectionMessage(id, newId);
        System.out.println("Sending message");
        clientConnectionHandler.sendMessage(message);
    }
    /**
     * Joins a saved game
     * @param name the name of the player to join as
     */
    public void joinSavedGame(String name) {
        SavedGameMessage savedGameMessage = new SavedGameMessage(id, name);
        clientConnectionHandler.sendMessage(savedGameMessage);
    }

    /**
     * Joins a server with the given ip and port
     * @param ip the ip of the server
     * @param port the port of the server
     */
    public void joinServer(String ip, int port) {
        try {
            clientConnectionHandler = new GeneralClientConnectionHandler(this, rmiMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Joining server");
        try{
            clientConnectionHandler.setSocket(ip, port);
            ui.successfulConnection();
        } catch (IOException | NotBoundException e){
            ui.connectionFailed();
        }
    }

    /**
     * Sends a message to the server setting the player's ready status to be true
     */
    public void setReady() {
        if (myName == null || getMyColor() == null) {
            ui.needNameOrColor();
            return;
        }
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, id);
        try {
            clientConnectionHandler.sendMessage(readyStatusMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server setting the player's color to the given color
     * @param color the color to set
     */
    public void askSetColor(String color) {
        if (this.myName == null) {
            ui.needName();
            return;
        }
        Color castedColor;
        try {
            castedColor = Color.valueOf(color.toUpperCase());
            if (unavaiableColors.contains(castedColor)){
                ui.unavailableColor();
                return;
            }
        } catch (IllegalArgumentException e) {
            ui.invalidColor();
            return;
        }
        proposedColor = castedColor;
        PlayerColorMessage playerColorMessage = new PlayerColorMessage(castedColor, id);
        try {
            clientConnectionHandler.sendMessage(playerColorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the chat
     * @param message the text of the message to send
     */
    public void sendChatMessage (String message){
        if (myName == null) {
            ui.needName();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message, id);
        clientConnectionHandler.sendMessage(chatMessage);
    }

    /**
     * Sends a message to the server asking to create a new player with the given name to play as
     * @param name the name of the player to play as
     */
    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, id);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            ui.nameChangeFailed();
        }
    }

    /**
     * Sets the RMI mode of the client: true if the client is in RMI mode, false if it is in Socket mode.
     * @param rmi the mode to set
     */
    public void setRMIMode(boolean rmi) {
        this.rmiMode = rmi;
    }

    /**
     * Sends a message to the server selecting the face of the starting card
     * @param face the face to select: FRONT or BACK
     */
    public void chooseStartingCardFace(Face face) {
        SetStartingCardMessage startingCardMessage = new SetStartingCardMessage(face, id);
        try {
            clientConnectionHandler.sendMessage(startingCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server selecting one of the possible secret achievements
     * @param index the index of the secret achievement to select: 0 for the first secret achievement, 1 for the second secret achievement
     */
    public void chooseSecretAchievement(int index) {

        this.indexofSecretAchievement = index;
        SetSecretCardMessage secretAchievementMessage = new SetSecretCardMessage(index, id);
        try {
            clientConnectionHandler.sendMessage(secretAchievementMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server asking to place a card
     * @param cardNumber the index of the card to play in the player's hand
     * @param face the face of the card to play: FRONT or BACK
     * @param x the x coordinate of the position where the card should be placed
     * @param y the y coordinate of the position where the card should be placed
     */
    public void askPlayCard(int cardNumber, Face face, int x, int y) {
        chosenHandCard = cardNumber;
        PlayCardMessage placeCardMessage = new PlayCardMessage(id, cardNumber, face, x, y);
        try {
            clientConnectionHandler.sendMessage(placeCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server asking to draw a card
     * @param deck the deck to draw from: GOLD or RESOURCE
     * @param deckPosition the position to draw from: DECK to draw from the deck itself, FIRST_CARD to draw from the first card on the board or SECOND_CARD to draw from the second card on the board
     */
    public void askDrawCard(Decks deck, DeckPosition deckPosition) {
        DrawCardMessage drawCardMessage = new DrawCardMessage(deckPosition, deck, id);
        try {
            clientConnectionHandler.sendMessage(drawCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ui getters

    /**
     * Gets the User's player's name
     * @return the name of the User's player
     */
    public String getMyName() {
        return myName;
    }
    /**
     * Gets the players in the game
     * @return a list of the players in the game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the current game state
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }
    /**
     * Gets the available colors
     * @return list of available colors
     */
    public List<Color> getAvailableColors(){
        List<Color> availableColors = new ArrayList<>();
        for (Color c : Color.values()){
            if (!unavaiableColors.contains(c)){
                availableColors.add(c);
            }
        }
        return availableColors;
    }

    /**
     * Gets the player's hand
     * @return the player's hand as a list of Card
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Gets the common achievements
     * @return the common achievements as a list of achievementCard
     */
    public List<AchievementCard> getCommonAchievements() {
        return commonAchievements;
    }

    /**
     * Gets the leaderboard
     * @return the leaderboard as a map of player names to points
     */
    public Map<String, Integer> getLeaderboard(){
        Map<String, Integer> leaderboard = new LinkedHashMap<>(); //linkedhashmap keeps order of insertion which will be the player order
        List<Player> orderedPlayers = players.stream().sorted((p1, p2) -> {
            if(p1.getPoints() == p2.getPoints())
                return p2.getAchievementPoints() - p1.getAchievementPoints();
            return p2.getPoints() - p1.getPoints();
        }).toList();
        for (Player p : orderedPlayers){
            leaderboard.put(p.getName(), p.getPoints());
        }
        return leaderboard;
    }

    /**
     * Gets a deck's size i.e. the number of cards left in the deck
     * @param deck the deck to get the size of: GOLD or RESOURCE
     * @return the size of the deck
     */
    public int getDeckSize(Decks deck){
        return switch (deck) {
            case GOLD -> goldDeck.getDeckSize();
            case RESOURCE -> resourceDeck.getDeckSize();
            default -> 0;
        };
    }

    /**
     * Gets the board cards of a deck
     * @param deck the deck to get the board cards of: GOLD or RESOURCE
     * @return the board cards of the deck in a list of Card
     */
    public List<Card> getBoardCards(Decks deck){
        List<Card> boardCards = new ArrayList<>();
        switch (deck){
            case GOLD:
                for (GoldCard c : goldDeck.getBoardCards()){
                    boardCards.add(c);
                }
            case RESOURCE:
                for (ResourceCard c : resourceDeck.getBoardCards()){
                    boardCards.add(c);
                }
        }
        return boardCards;
    }

    /**
     * Gets the player who is currently active i.e. whose turn it is
     * @return the active player | null if no player is active
     */
    public Player getActivePlayer(){
        for (Player p : players){
            if (p.isActive()) return p;
        }
        return null;
    }

    /**
     * gets the current turn number
     * @return the current turn number
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Gets the names of the players in the game
     * @return a list of the names of the players in the game
     */
    public List<String> getPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        for (Player p : players){
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    //methods called by incoming messages

    /**
     * Loads the data needed while in the lobby. This is called by a LobbyPlayersMessage when the client connects successfully to a server
     * @param id the client id needed to reconnect to the game
     * @param playerNames a list of the names of the players in the game
     * @param playerColors a map of the players' names to their colors
     * @param playerReady a map of the players' names to their ready status
     * @param isSavedGame whether the game is a saved game or a new game
     */
    public void loadLobbyInfo(String id, List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, Boolean isSavedGame) {
        this.isSavedGame = isSavedGame;
        setId(id);
        ui.displayId();
        for (String name : playerNames){
            Player p = new Player(name);
            p.setColor(playerColors.get(name));
            p.setReady(playerReady.get(name));
            players.add(p);
        }
        ui.displayLobby();
    }

    /**
     * Sets the common and possible secret achievement cards
     * @param secretCards a list of the possible secret achievement cards to choose from
     * @param commonCards a list of the common achievement cards
     */
    public void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards) {
        gameState = GameState.CHOOSE_SECRET_ACHIEVEMENT;
        commonAchievements = new ArrayList<>();
        potentialSecretAchievements = secretCards;
        for (Card c : commonCards){
            commonAchievements.add((AchievementCard) c);
        }
        ui.displayCommonAchievements();
        ui.chooseSecretAchievement(secretCards);
    }

    /**
     * Displays that the player cannot draw from the achievement deck
     */
    public void achievementDeckDrawInvalid() {
        ui.cantDrawAchievementCards();
    }

    /**
     * Displays that the action the player is trying to do has already been done
     * @param action the action that has already been done
     */
    public void alreadyDone(Actions action) {
        ui.alreadyDone(action);
    }

    /**
     * Displays that a card is not placeable
     */
    public void cardNotPlaceable() {
        ui.cardNotPlaceable();
    }

    /**
     * Appends a message to the chat
     * @param message the message to add
     */
    public void addChatMessage(Message message){
        chat.addMessage(message);
        ui.chat(message);
    }

    /**
     * Displays that the user cannot send an empty chat message
     */
    public void chatMessageIsEmpty() {
        ui.chatMessageIsEmpty();
    }

    /**
     * Displays that the user cannot do what they are trying to do without setting their color first
     */
    public void colorNotYetSet() {
        ui.needColor();
    }

    /**
     * Adds a card to the player's hand
     * @param drawnCard the card to add
     */
    public void giveDrawnCard(Card drawnCard) {
        hand.add((ResourceCard) drawnCard);
        ui.displayNewCardInHand();
    }

    /**
     * Displays that a deck is empty
     */
    public void emptyDeck() {
        ui.deckIsEmpty();
    }

    /**
     * Displays that the game is in the end phase
     */
    public void setEndGame() {
        ui.endGameStarted();
    }

    /**
     * Displays that the game has already started so the player cannot do what they are trying to do
     */
    public void gameAlreadyStarted() {
        ui.gameAlreadyStarted();
    }

    /**
     * Displays that the game has not yet started so the player cannot do what they are trying to do
     */
    public void gameNotYetStarted() {
        ui.gameNotYetStarted();
    }

    /**
     * Gives the player their initial hand
     * @param hand the initial hand to give
     */
    public void giveInitialHand(List<Card> hand) {
        for (Card c : hand){
            this.hand.add((ResourceCard) c);
        }
        ui.displayHand();
    }

    /**
     * Displays that the card selected is invalid for a certain action
     * @param cardType the action the card is invalid for
     */
    public void invalidCard(Actions cardType) {
        ui.invalidCardForAction(cardType);
    }

    /**
     * Displays the leaderboard
     * @param playerPoints a map of the player names to their points, in the order of the leaderboard
     */
    public void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints) {
        gameState = GameState.LEADERBOARD;
        for (String name : playerPoints.keySet()){
            try {
                getPlayerByName(name).setAchievementPoints(playerPoints.get(name) - getPlayerByName(name).getPoints());
            } catch (PlayerNotFoundByNameException e) {
                throw new RuntimeException(e);
            }
        }
        ui.displayLeaderboard(playerPoints);
    }

    /**
     * Displays thet the player cannot do what they are trying to do without setting their name first
     */
    public void nameNotYetSet() {
        ui.needName();
    }

    /**
     * Add a new player to the game
     * @param playerNames the new list of player names
     */
    public void newPlayer(List<String> playerNames) {
        Player newPlayer = new Player(playerNames.getLast());
        players.add(newPlayer);
        ui.displayNewPlayer();
    }

    /**
     * Displays that the player doesn't have a card yet to do a certain action
     * @param type the action the player doesn't have a card for
     */
    public void notYetGivenCard(Actions type) {
        ui.notYetGivenCard(type);
    }

    /**
     * Displays that it's not the player's turn
     */
    public void notYourTurn() {
        ui.notYourTurn();
    }

    /**
     * Handles the start of a new turn
     * @param activePlayerName the name of the player whose turn it is
     * @param turnNumber the number of the turn
     */
    public void newTurn(String activePlayerName, int turnNumber) {
        if(!gameState.equals(GameState.LEADERBOARD)) {
            gameState = GameState.PLACE_CARD;
            turn = turnNumber;
            for (Player p : players) {
                p.setActive(p.getName().equals(activePlayerName));
            }
            ui.newTurn();
        }
    }

    /**
     * Handles another player drawing a card
     * @param name the name of the player who drew the card
     * @param deckFrom the deck the card was drawn from: GOLD or RESOURCE
     * @param drawPosition the position the card was drawn from: DECK, FIRST_CARD or SECOND_CARD
     * @param newBoardCards the new board cards of the corrisponding deck after the card was drawn
     */
    public void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards){
        switch (deckFrom){
            case GOLD:
                goldDeck.setBoardCards(newBoardCards);
                goldDeck.setDeckSize(goldDeck.getDeckSize() - 1);
                break;
            case RESOURCE:
                resourceDeck.setBoardCards(newBoardCards);
                resourceDeck.setDeckSize(resourceDeck.getDeckSize() - 1);
                break;
        }
        try{
            getPlayerByName(name).setHandSize(getPlayerByName(name).getHandSize() + 1);
            ui.otherPlayerDraw(name, deckFrom, drawPosition);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles another player receiving their initial hand
     * @param name the name of the player who received their initial hand
     */
    public void giveOtherPlayerInitialHand(String name){
        try {
            Player p = getPlayerByName(name);
            p.setHandSize(3);
            if (resourceDeck == null || goldDeck == null){
                resourceDeck = new Deck<ResourceCard>();
                goldDeck = new Deck<GoldCard>();
            }
            resourceDeck.setDeckSize(resourceDeck.getDeckSize() - 2);
            goldDeck.setDeckSize(goldDeck.getDeckSize() - 1);
            ui.otherPlayerInitialHand(name);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the player's color
     * @param confirmation whether the color was set successfully
     * @param color the color that was set
     */
    public void setColor(boolean confirmation, Color color){
        if(confirmation){
            setMyColor(color);
            ui.colorChanged();
        } else {
            ui.colorChangeFailed();
        }
    }
    /**
     * Updates the player with the new color and tells the user which players have which colors
     * @param color the new color
     * @param name the name of the player
     */
    public void updatePlayerColors(Color color, String name) {
        try {
            getPlayerByName(name).setColor(color);
            ui.displayPlayerColors();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the player's name to the proposed name if the name was set successfully
     * @param confirmation whether the name was set successfully
     */
    public void setName(Boolean confirmation){
        if(confirmation){
            this.myName = this.proposedName;
            ui.nameChanged(myName);
        } else {
            ui.nameChangeFailed();
        }
    }
    /**
     * Updates the player with the new ready status
     * @param ready new ready status
     * @param name name of the player
     */
    public void updatePlayerReady(boolean ready, String name) {
        try {
            getPlayerByName(name).setReady(ready);
            ui.updateReady(name, ready);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles a secret card choice, which can be done by the player themselves or by another player
     * @param name the name of the player who chose the secret card
     */
    public void setSecretCard(String name) {
        System.out.println("myname " + myName);
        if (name.equals(myName)){
            secretAchievement = potentialSecretAchievements.get(indexofSecretAchievement);
        }
        potentialSecretAchievements.stream().forEach(System.out::println);
        System.out.println(indexofSecretAchievement + " index " + secretAchievement + " secret");
        System.out.println("Secret achievement chosen in ClientController");
        ui.secretAchievementChosen(name);
    }

    /**
     * Sets the secret card for the player
     * @param chosenCard the index of the secret card to set
     */
    public void setSecretCard(int chosenCard){
        indexofSecretAchievement = chosenCard;
        setSecretCard(myName);
    }

    /**
     * Handles the starting card choice, which can be done by the player themselves or by another player
     * @param name the name of the player who chose the starting card
     * @param startingFace the face selected
     */
    public void startingCardChosen(String name, CornerCardFace startingFace) {
        try{
            getPlayerByName(name).initializeManuscript(startingFace);
            ui.startingCardChosen(name);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the board cards and displays the game start
     * @param goldBoardCards the gold cards on the board
     * @param resourceBoardCards the resource cards on the board
     */
    public void startGame(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards){
        ui.gameStarted();
        goldDeck = new Deck<>(goldBoardCards);
        resourceDeck = new Deck<>(resourceBoardCards);
        ui.displayBoardCards();
    }

    /**
     * Updates the order of the players list
     * @param playerNames the new order of the players as a list of names
     */
    public void updatePlayerOrder(List<String> playerNames){
        List<Player> newPlayerList = new ArrayList<>();
        try {
            for (String name : playerNames) {
                newPlayerList.add(getPlayerByName(name));
            }
            players = newPlayerList;
            gameState = GameState.PLACE_CARD;
            ui.displayPlayerOrder();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the starting card being given to the player
     * @param card the card given to the player
     */
    public void giveStartingCard(Card card) {
        gameState = GameState.CHOOSE_STARTING_CARD;
        ui.chooseStartingCardFace(card);
    }

    /**
     * Displays that the player cannot do what they are trying to do without doing something else first
     * @param actionToDo the action the player needs to do first
     */
    public void toDoFirst(Actions actionToDo) {
        ui.doFirst(actionToDo);
    }

    /**
     * Handles the placement of a card on the board by a player
     * @param playerName the name of the player who placed the card
     * @param placedCardFace the face placed
     * @param x the x coordinate of the position where the card was placed
     * @param y the y coordinate of the position where the card was placed
     * @param points the points the player received for placing the card
     */
    public void placeCard(String playerName, CornerCardFace placedCardFace, int x, int y, int points){
        try{
            getPlayerByName(playerName).addManuscriptPoints(points);
            getPlayerByName(playerName).addCardToManuscript(x, y, placedCardFace, turn);
            getPlayerByName(playerName).setHandSize(getPlayerByName(playerName).getHandSize() - 1);
            // RIMUOVI LA CARTA DALLA MANO SE IL PLAYER SONO IO
            if (playerName.equals(myName)) {
                hand.remove(chosenHandCard.intValue());
            }
            setChosenHandCard(null);
            ui.cardPlaced(playerName, placedCardFace, x, y);
            if(points > 0){
                ui.displayPlayerPoints(playerName);
            }
            gameState = GameState.DRAW_CARD;
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays a player's disconnection
     * @param playerName the name of the player who disconnected
     */
    public void playerDisconnected(String playerName) {
        ui.playerDisconnected(playerName);
    }

    /**
     * Displays that the chosen name is invalid
     */
    public void invalidName() {
        ui.nameChangeFailed();
    }

    /**
     * Displays that there are too many players in the game
     */
    public void tooManyPlayers() {
        ui.tooManyPlayers();
    }

    /**
     * Handles the remotion of a player from the game
     * @param name the name of the player who was removed
     */
    public void playerRemoved(String name) {
        players = players.stream().filter(p -> !p.getName().equals(name)).toList();
        ui.playerRemoved(name);
    }

    /**
     * Displays the reconnection of a player
     * @param name the name of the player who reconnected
     */
    public void otherPlayerReconnected(String name) {
        ui.otherPlayerReconnected(name);
    }

    /**
     * Displays that the chosen id is not in the game
     */
    public void idNotInGame(){
        ui.idNotInGame();
    }

    /**
     * Displays that the player is already playing
     */
    public void playerAlreadyPlaying(){
        ui.playerAlreadyPlaying();
    }

    /**
     * Gets the secret achievement card
     * @return the secret achievement card
     */
    public AchievementCard getSecretAchievement() {
        return secretAchievement;
    }

    /**
     * Gets all of the chat
     * @return all the messages in the chat as a list of Message
     */
    public List<Message> getChat() {
        return chat.getMessages();
    }

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
    public void setGameInfo(String id, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, String name, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState) {


        this.id = id;
        this.commonAchievements = commonAchievements;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.myName = name;
        this.secretAchievement = secretAchievement;
        this.hand = hand;
        this.turn = turn;
        this.players = players;
        this.chat = chat;
        this.gameState = gameState;
        this.unavaiableColors = new ArrayList<>();
        for (Player p : players){
            unavaiableColors.add(p.getColor());
        }
        System.out.println("Game info set");
        ui.displayGameInfo();
    }

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
    public void loadGame(List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState, String name) {
        this.commonAchievements = commonAchievements;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.secretAchievement = secretAchievement;
        this.hand = hand;
        this.turn = turn;
        this.players = players;
        this.chat = chat;
        this.gameState = gameState;
        this.myName = name;
        this.unavaiableColors = new ArrayList<>();
        for (Player p : players){
            unavaiableColors.add(p.getColor());
        }
        ui.displayGameInfo();
    }

    /**
     * Displays that the game has already finished
     */
    public void gameAlreadyFinished() {
        ui.gameAlreadyFinished();
    }
}
