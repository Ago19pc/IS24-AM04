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

public class ClientControllerInstance implements ClientController {
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

    @Override
    public void main(UI ui) throws RemoteException {

        this.ui = ui;
        this.gameState = GameState.LOBBY;
    }

    /**
     * @return true if you're joining a saved game
     */
    @Override
    public boolean isSavedGame() {
        return isSavedGame;
    }

    /**
     * Sets the name of the player
     * @param name
     */
    @Override
    public void setName(String name) {
        this.myName = name;
    }

    /**
     * @return the list of card from witch you can choose the secret achievement
     */
    @Override
    public List<AchievementCard> getPotentialSecretAchievements() {
        return potentialSecretAchievements;
    }

    /**
     * Sets the selected handcard
     * @param chosenHandCard, the position of the card in hand
     */
    @Override
    public void setChosenHandCard(Integer chosenHandCard) {
        this.chosenHandCard = chosenHandCard;
    }

    /**
     * @return the selected handcard
     */
    @Override
    public Integer getChosenHandCard() {
        return chosenHandCard;
    }

    //helper methods

    /**
     * Gets the player with the given name
     * @param name, the name of the player
     * @return player, the player with the provided name
     * @throws PlayerNotFoundByNameException if player can't be found
     */
    @Override
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    /**
     * Gets the color of the player
     * @return color, the color of the player
     */
    @Override
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
     * Gets the id of the player
     * @return id, the id of the player
     */
    @Override
    public String getMyId() {
        return id;
    }

    /**
     * Sets the color of the player
     * @param color, the color
     */
    @Override
    public void setMyColor(Color color) {
        try {
            getPlayerByName(myName).setColor(color);
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the id of the player
     * @param id, the ID
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    //ui actions

    /**
     * Called when you want to reconnect to a game
     * @param newId, the old id of the player
     */
    @Override
    public void reconnect(String newId) {
        ReconnectionMessage message = new ReconnectionMessage(id, newId);
        clientConnectionHandler.sendMessage(message);
    }

    /**
     * Called when you want to join a game that was saved
     * @param name, your name
     */
    @Override
    public void joinSavedGame(String name) {
        SavedGameMessage savedGameMessage = new SavedGameMessage(id, name);
        clientConnectionHandler.sendMessage(savedGameMessage);
    }

    /**
     * Called when you want to join a game
     * @param ip, the ip of the server
     * @param port, the port of the server (watch out! it should be the port for what kind of connection you choose, RMI or SOCKET)
     */
    @Override
    public void joinServer(String ip, int port) {
        try {
            clientConnectionHandler = new GeneralClientConnectionHandler(this, rmiMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try{
            clientConnectionHandler.setSocket(ip, port);
            ui.successfulConnection();
        } catch (IOException | NotBoundException e){
            ui.connectionFailed();
        }
    }

    /**
     * Called when you want to be Ready to start the game
     */
    @Override
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
     * Called when you choose a color, the server will respond if your choice is legal
     * @param color, the color you are asking for
     */
    @Override
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
     * Called when you want to send a chat message
     * @param message, the message you want to send
     */
    @Override
    public void sendChatMessage(String message){
        if (myName == null) {
            ui.needName();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message, id);
        clientConnectionHandler.sendMessage(chatMessage);
    }

    /**
     * Called when you want to choose your name, you're asking if it's ok for the server
     * @param name, the name you want
     */
    @Override
    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, true, id);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            ui.nameChangeFailed();
        }
    }

    /**
     * Do you want to connect via RMI?
     * @param rmi, true if you want RMI
     */
    @Override
    public void setRMIMode(boolean rmi) {
        this.rmiMode = rmi;
    }

    /**
     * Choose the starting card face
     * @param face, the face (FRONT or BACK)
     */
    @Override
    public void chooseStartingCardFace(Face face) {
        SetStartingCardMessage startingCardMessage = new SetStartingCardMessage(face, id);
        try {
            clientConnectionHandler.sendMessage(startingCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Choose the secret achievement card
     * @param index, which card you want to choose
     */
    @Override
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
     * Ask the server if the placement you want to make is possible
     * @param cardNumber the index of the card in your hand
     * @param face front or back
     * @param x x coord
     * @param y y coord
     */
    @Override
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
     * Ask the server to draw a card
     * @param deck which deck? Gold or Resource
     * @param deckPosition from where? Deck, First or Second
     */
    @Override
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
     * Index of the secretAchievementCard chosen
     * @return the index
     */
    @Override
    public int getIndexofSecretAchievement() {
        return indexofSecretAchievement;
    }

    /**
     * Getter for the name
     * @return myName
     */
    @Override
    public String getMyName() {
        return myName;
    }

    /**
     * Getter for list of players
     * @return the list of players
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getter for the gameState
     * @return the gameState
     */
    @Override
    public GameState getGameState() {
        return gameState;
    }
    /**
     * Gets the available colors
     * @return list of available colors
     */
    @Override
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
     * Gets the list of card in your hand
     * @return List<Card> handCards
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * @return List<AchievementCard> commonAchievements, the achievements common to all players
     */
    @Override
    public List<AchievementCard> getCommonAchievements() {
        return commonAchievements;
    }

    /**
     * @return the ordered leatherboard as a map
     */
    @Override
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
     * Get how many cards are left in the selected deck
     * @param deck Gold or Resource
     * @return the quantity
     */
    @Override
    public int getDeckSize(Decks deck){
        return switch (deck) {
            case GOLD -> goldDeck.getDeckSize();
            case RESOURCE -> resourceDeck.getDeckSize();
            default -> 0;
        };
    }

    /**
     * Get the card on the board from the selected deck
     * @param deck Gold or Resource
     * @return List<Card>
     */
    @Override
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
     * Get the player that is currently playing
     * @return the player
     */
    @Override
    public Player getActivePlayer(){
        for (Player p : players){
            if (p.isActive()) return p;
        }
        return null;
    }

    /**
     * Get the turn number
     * @return the turn number
     */
    @Override
    public int getTurn() {
        return turn;
    }

    /**
     * Get the list of player names
     * @return List of names
     */
    @Override
    public List<String> getPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        for (Player p : players){
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    //methods called by incoming messages

    /**
     * Loads data of the lobby, such as player data and if it's a saved game
     * @param id your id
     * @param playerNames the list of playernames
     * @param playerColors the color of each player
     * @param playerReady the ready status of each player
     * @param isSavedGame if it's a saved game
     */
    @Override
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
     * Give the player all the achievement, the list of secret and common
     * @param secretCards
     * @param commonCards
     */
    @Override
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
     * Error message
     * You cannot draw from the achievement deck
     */
    @Override
    public void achievementDeckDrawInvalid() {
        ui.cantDrawAchievementCards();
    }

    /**
     * Error message
     * You already did this action and cannot do it again!
     */
    @Override
    public void alreadyDone(Actions action) {
        ui.alreadyDone(action);
    }

    /**
     * You can't place the card! Reason unspecified
     */
    @Override
    public void cardNotPlaceable() {
        ui.cardNotPlaceable();
    }

    /**
     * Send a message to the chat
     * @param message
     */
    @Override
    public void addChatMessage(Message message){
        chat.addMessage(message);
        ui.chat(message);
    }

    /**
     * ERROR
     * The chat message is empty
     */
    @Override
    public void chatMessageIsEmpty() {
        ui.chatMessageIsEmpty();
    }

    /**
     * ERROR
     * You need to set your color first
     */
    @Override
    public void colorNotYetSet() {
        ui.needColor();
    }

    /**
     * Add the card to the hand
     * @param drawnCard, the drawn card
     */
    @Override
    public void giveDrawnCard(Card drawnCard) {
        hand.add((ResourceCard) drawnCard);
        ui.displayNewCardInHand();
    }

    /**
     * ERROR
     * You are trying to draw from an empty deck
     */
    @Override
    public void emptyDeck() {
        ui.deckIsEmpty();
    }

    /**
     * End game is started
     */
    @Override
    public void setEndGame() {
        ui.endGameStarted();
    }

    /**
     * Game has already started
     */
    @Override
    public void gameAlreadyStarted() {
        ui.gameAlreadyStarted();
    }

    /**
     * Game has not started yet
     */
    @Override
    public void gameNotYetStarted() {
        ui.gameNotYetStarted();
    }

    /**
     * Se the player's hand
     * @param hand List
     */
    @Override
    public void giveInitialHand(List<Card> hand) {
        for (Card c : hand){
            this.hand.add((ResourceCard) c);
        }
        ui.displayHand();
    }

    /**
     * ERROR
     * The card you choose is invalid for the action
     */
    @Override
    public void invalidCard(Actions cardType) {
        ui.invalidCardForAction(cardType);
    }

    /**
     * Display the leaderboard when game is finished
     * @param playerPoints
     */
    @Override
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
     * You haven't set your name yet
     */
    @Override
    public void nameNotYetSet() {
        ui.needName();
    }

    /**
     * Adds a player and displays it
     * @param playerNames
     */
    @Override
    public void newPlayer(List<String> playerNames) {
        Player newPlayer = new Player(playerNames.getLast());
        players.add(newPlayer);
        ui.displayNewPlayer();
    }


    @Override
    public void notYetGivenCard(Actions type) {
        ui.notYetGivenCard(type);
    }
    @Override
    public void notYourTurn() {
        ui.notYourTurn();
    }
    @Override
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
    @Override
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
    @Override
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
    @Override
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
     */
    @Override
    public void updatePlayerColors(Color color, String name) {
        try {
            getPlayerByName(name).setColor(color);
            ui.displayPlayerColors();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
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
    @Override
    public void updatePlayerReady(boolean ready, String name) {
        try {
            getPlayerByName(name).setReady(ready);
            ui.updateReady(name, ready);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the secret card to player
     * @param name
     */
    @Override
    public void setSecretCard(String name) {
        if (name.equals(myName)){
            secretAchievement = potentialSecretAchievements.get(indexofSecretAchievement);
        }
        ui.secretAchievementChosen(name);
    }

    /**
     * Sets the secret card to player
     * @param name
     * @param chosenCard
     */
    @Override
    public void setSecretCard(String name, int chosenCard){
        if (name.equals(myName)){
            indexofSecretAchievement = chosenCard;
            setSecretCard(name);
        }
    }

    /**
     * Sets the starting card
     * @param name
     * @param startingFace
     */
    @Override
    public void startingCardChosen(String name, CornerCardFace startingFace) {
        try{
            getPlayerByName(name).initializeManuscript(startingFace);
            ui.startingCardChosen(name);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Game has started, adds the cards to the decks
     * @param goldBoardCards
     * @param resourceBoardCards
     */
    @Override
    public void startGame(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards){
        ui.gameStarted();
        goldDeck = new Deck<>(goldBoardCards);
        resourceDeck = new Deck<>(resourceBoardCards);
        ui.displayBoardCards();
    }

    /**
     * Updated the play order
     * @param playerNames list of ordered names
     */
    @Override
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
     * Give to the player the stating card
     * @param card
     */
    @Override
    public void giveStartingCard(Card card) {
        gameState = GameState.CHOOSE_STARTING_CARD;
        ui.chooseStartingCardFace(card);
    }

    /**
     * You need to do an action first!
     * @param actionToDo, the action to do first
     */
    @Override
    public void toDoFirst(Actions actionToDo) {
        ui.doFirst(actionToDo);
    }

    /**
     * Place the card on the manuscript
     * @param playerName
     * @param placedCardFace
     * @param x
     * @param y
     * @param points
     */
    @Override
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
     * Player has disconnected
     * @param playerName
     */
    @Override
    public void playerDisconnected(String playerName) {
        ui.playerDisconnected(playerName);
    }

    /**
     * The name you choose is invalid
     */
    @Override
    public void invalidName() {
        ui.nameChangeFailed();
    }

    /**
     * The match is full
     */
    @Override
    public void tooManyPlayers() {
        ui.tooManyPlayers();
    }

    /**
     * Player was removed
     * @param name
     */
    @Override
    public void playerRemoved(String name) {
        players = players.stream().filter(p -> !p.getName().equals(name)).toList();
        ui.playerRemoved(name);
    }

    /**
     * Other player has disconnected
     * @param name
     */
    @Override
    public void otherPlayerReconnected(String name) {
        ui.otherPlayerReconnected(name);
    }

    /**
     * The id is not valid
     */
    @Override
    public void idNotInGame(){
        ui.idNotInGame();
    }

    /**
     * Player is already playing
     */
    @Override
    public void playerAlreadyPlaying(){
        ui.playerAlreadyPlaying();
    }

    /**
     * Get the player's secret achievement
     * @return
     */
    @Override
    public AchievementCard getSecretAchievement() {
        return secretAchievement;
    }

    /**
     * Get the list of messages in the chat
     * @return
     */
    @Override
    public List<Message> getChat() {
        return chat.getMessages();
    }

    /**
     * Loads data
     * @param id
     * @param commonAchievements
     * @param goldDeck
     * @param resourceDeck
     * @param name
     * @param secretAchievement
     * @param hand
     * @param turn
     * @param players
     * @param chat
     * @param gameState
     */
    @Override
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
        ui.displayGameInfo();
    }

    /**
     * Loads the data
     * @param commonAchievements
     * @param goldDeck
     * @param resourceDeck
     * @param secretAchievement
     * @param hand
     * @param turn
     * @param players
     * @param chat
     * @param gameState
     * @param name
     */
    @Override
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
     * Game has already finished
     */
    @Override
    public void gameAlreadyFinished() {
        ui.gameAlreadyFinished();
    }
}
