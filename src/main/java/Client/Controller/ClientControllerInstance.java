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
 * Implementation of the ClientController interface.
 */
public class ClientControllerInstance implements ClientController {
    private GeneralClientConnectionHandler clientConnectionHandler;
    private UI ui;

    //cards not owned by player info
    private List<AchievementCard> commonAchievements = new ArrayList<>();
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
    private int indexofSecretAchievement = -1;
    private List<AchievementCard> potentialSecretAchievements = new ArrayList<>();
    private Integer chosenHandCard;
    private Boolean isSavedGame;

    /**
     * Constructor
     */
    public ClientControllerInstance() {}

    @Override
    public void main(UI ui) {

        this.ui = ui;
        this.gameState = GameState.LOBBY;
    }

    @Override
    public boolean isSavedGame() {
        return isSavedGame;
    }

    @Override
    public void setName(String name) {
        this.myName = name;
    }

    @Override
    public List<AchievementCard> getPotentialSecretAchievements() {
        return potentialSecretAchievements;
    }

    @Override
    public void setChosenHandCard(Integer chosenHandCard) {
        this.chosenHandCard = chosenHandCard;
    }

    @Override
    public Integer getChosenHandCard() {
        return chosenHandCard;
    }

    //helper methods

    @Override
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    @Override
    public Color getMyColor() {
        Color myColor = null;
        try {
            myColor = getPlayerByName(myName).getColor();
        } catch (PlayerNotFoundByNameException e) {
            System.err.println("Error finding player in getMyColor");
        }
        return myColor;
    }

    @Override
    public String getMyId() {
        return id;
    }

    @Override
    public void setMyColor(Color color) {
        try {
            getPlayerByName(myName).setColor(color);
        } catch (PlayerNotFoundByNameException e) {
            System.err.println("Error finding player in setMyColor");
        }
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    //ui actions

    @Override
    public void reconnect(String newId) {
        ReconnectionMessage message = new ReconnectionMessage(id, newId);
        clientConnectionHandler.sendMessage(message);
    }

    @Override
    public void joinSavedGame(String name) {
        SavedGameMessage savedGameMessage = new SavedGameMessage(id, name);
        clientConnectionHandler.sendMessage(savedGameMessage);
    }

    @Override
    public void joinServer(String ip, int port) {
        try{
            clientConnectionHandler.setSocket(ip, port);
        } catch (IOException | NotBoundException | NullPointerException e){
            ui.connectionFailed();
        }
    }

    @Override
    public void setReady() {
        if (myName == null || getMyColor() == null) {
            ui.needNameOrColor();
            return;
        }
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, id);
        clientConnectionHandler.sendMessage(readyStatusMessage);

    }

    @Override
    public void askSetColor(Color color) {
        if (this.myName == null) {
            ui.needName();
            return;
        }
        try {
            if (unavaiableColors.contains(color)){
                ui.unavailableColor();
                return;
            }
        } catch (IllegalArgumentException e) {
            ui.invalidColor();
            return;
        }
        PlayerColorMessage playerColorMessage = new PlayerColorMessage(color, id);
        clientConnectionHandler.sendMessage(playerColorMessage);

    }

    @Override
    public void sendChatMessage(String message){
        if (myName == null) {
            ui.needName();
            return;
        }
        if(message.isEmpty()){
            ui.chatMessageIsEmpty();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message, id);
        clientConnectionHandler.sendMessage(chatMessage);
    }

    @Override
    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, id);
        clientConnectionHandler.sendMessage(playerNameMessage);
    }

    @Override
    public void setRMIMode(boolean rmi) {
        try {
            if(clientConnectionHandler != null) {
                clientConnectionHandler.clear(rmi);
            } else {
                clientConnectionHandler = new GeneralClientConnectionHandler(this, rmi);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseStartingCardFace(Face face) {
        SetStartingCardMessage startingCardMessage = new SetStartingCardMessage(face, id);
        clientConnectionHandler.sendMessage(startingCardMessage);
    }

    @Override
    public void chooseSecretAchievement(int index) {

        this.indexofSecretAchievement = index;
        SetSecretCardMessage secretAchievementMessage = new SetSecretCardMessage(index, id);
        clientConnectionHandler.sendMessage(secretAchievementMessage);

    }

    @Override
    public void askPlayCard(int cardNumber, Face face, int x, int y) {
        chosenHandCard = cardNumber;
        PlayCardMessage placeCardMessage = new PlayCardMessage(id, cardNumber, face, x, y);
        clientConnectionHandler.sendMessage(placeCardMessage);
    }

    @Override
    public void askDrawCard(Decks deck, DeckPosition deckPosition) {
        DrawCardMessage drawCardMessage = new DrawCardMessage(deckPosition, deck, id);
        clientConnectionHandler.sendMessage(drawCardMessage);
    }

    //ui getters

    @Override
    public String getMyName() {
        return myName;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

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

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
    public List<AchievementCard> getCommonAchievements() {
        return commonAchievements;
    }

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

    @Override
    public int getDeckSize(Decks deck){
        return switch (deck) {
            case GOLD -> goldDeck.getDeckSize();
            case RESOURCE -> resourceDeck.getDeckSize();
            default -> 0;
        };
    }

    @Override
    public List<Card> getBoardCards(Decks deck){
        List<Card> boardCards = new ArrayList<>();
        switch (deck){
            case GOLD:
                boardCards.addAll(goldDeck.getBoardCards());
            case RESOURCE:
                boardCards.addAll(resourceDeck.getBoardCards());
        }
        return boardCards;
    }

    @Override
    public Player getActivePlayer(){
        for (Player p : players){
            if (p.isActive()) return p;
        }
        return null;
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public List<String> getPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        for (Player p : players){
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    //methods called by incoming messages

    @Override
    public void loadLobbyInfo(String id, List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, Boolean isSavedGame) {
        ui.successfulConnection();
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

    @Override
    public void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards) {
        gameState = GameState.CHOOSE_SECRET_ACHIEVEMENT;
        commonAchievements = new ArrayList<>();
        potentialSecretAchievements = secretCards;
        commonAchievements.addAll(commonCards);
        ui.displayCommonAchievements();
        ui.chooseSecretAchievement(secretCards);
    }

    @Override
    public void achievementDeckDrawInvalid() {
        ui.cantDrawAchievementCards();
    }

    @Override
    public void alreadyDone(Actions action) {
        ui.alreadyDone(action);
    }

    @Override
    public void cardNotPlaceable() {
        ui.cardNotPlaceable();
    }

    @Override
    public void addChatMessage(Message message){
        chat.addMessage(message);
        ui.chat(message);
    }

    @Override
    public void chatMessageIsEmpty() {
        ui.chatMessageIsEmpty();
    }

    @Override
    public void colorNotYetSet() {
        ui.needColor();
    }

    @Override
    public void giveDrawnCard(Card drawnCard) {
        hand.add(drawnCard);
        ui.displayNewCardInHand();
    }

    @Override
    public void emptyDeck() {
        ui.deckIsEmpty();
    }

    @Override
    public void setEndGame() {
        ui.endGameStarted();
    }

    @Override
    public void gameAlreadyStarted() {
        ui.gameAlreadyStarted();
    }

    @Override
    public void gameNotYetStarted() {
        ui.gameNotYetStarted();
    }

    @Override
    public void giveInitialHand(List<Card> hand) {
        this.hand.addAll(hand);
        ui.displayHand();
    }

    @Override
    public void invalidCard(Actions cardType) {
        ui.invalidCardForAction(cardType);
    }

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

    @Override
    public void nameNotYetSet() {
        ui.needName();
    }

    @Override
    public void newPlayer(List<String> playerNames) {
        try {
            Player newPlayer = new Player(playerNames.getLast());
            players.add(newPlayer);
            ui.displayNewPlayer();
        } catch (Exception e) {
            System.err.println("Error adding new player");
        }
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
                goldDeck.setDeckSize(Math.max(0, goldDeck.getDeckSize() - 1));
                break;
            case RESOURCE:
                resourceDeck.setBoardCards(newBoardCards);
                resourceDeck.setDeckSize(Math.max(0, resourceDeck.getDeckSize() - 1));
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
                resourceDeck = new Deck<>();
                goldDeck = new Deck<>();
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
            ui.nameChanged();
        } else {
            ui.nameChangeFailed();
        }
    }

    @Override
    public void updatePlayerReady(boolean ready, String name) {
        try {
            getPlayerByName(name).setReady(ready);
            ui.updateReady(name, ready);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setSecretCard(String name) {
        if (name.equals(myName)){
            secretAchievement = potentialSecretAchievements.get(indexofSecretAchievement);
        }
        ui.secretAchievementChosen(name);
    }

    @Override
    public void setSecretCard(int chosenCard){
        indexofSecretAchievement = chosenCard;
    }


    @Override
    public void startingCardChosen(String name, CornerCardFace startingFace) {
        try{
            getPlayerByName(name).initializeManuscript(startingFace);
            ui.startingCardChosen(name);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void startGame(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards){
        ui.gameStarted();
        goldDeck = new Deck<>(goldBoardCards);
        resourceDeck = new Deck<>(resourceBoardCards);
        ui.displayBoardCards();
    }

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

    @Override
    public void giveStartingCard(Card card) {
        gameState = GameState.CHOOSE_STARTING_CARD;
        ui.chooseStartingCardFace(card);
    }

    @Override
    public void toDoFirst(Actions actionToDo) {
        ui.doFirst(actionToDo);
    }

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

    @Override
    public void playerDisconnected(String playerName) {
        ui.playerDisconnected(playerName);
    }

    @Override
    public void invalidName() {
        ui.nameChangeFailed();
    }

    @Override
    public void tooManyPlayers() {
        ui.tooManyPlayers();
    }

    @Override
    public void playerRemoved(String name) {
        try {
            players.remove(getPlayerByName(name));
            ui.playerRemoved(name);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void otherPlayerReconnected(String name) {
        ui.otherPlayerReconnected(name);
    }

    @Override
    public void idNotInGame(){
        ui.idNotInGame();
    }

    @Override
    public void playerAlreadyPlaying(){
        ui.playerAlreadyPlaying();
    }

    @Override
    public AchievementCard getSecretAchievement() {
        return secretAchievement;
    }

    @Override
    public List<Message> getChat() {
        return chat.getMessages();
    }

    @Override
    public void setGameInfo(String id, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, String name, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState) {

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

    @Override
    public void gameAlreadyFinished() {
        ui.gameAlreadyFinished();
    }

    @Override
    public int getIndexofSecretAchievement() {
        return indexofSecretAchievement;
    }

    @Override
    public void serverDisconnected() {
        clear();
        ui.serverDisconnected();
        if(gameState != GameState.LEADERBOARD){
            clear();
        }
    }


    public void clear(){
        commonAchievements.clear();
        goldDeck = null;
        resourceDeck = null;
        myName = null;
        secretAchievement = null;
        hand.clear();
        turn = 0;
        unavaiableColors.clear();
        chat = new Chat();
        players.clear();
        proposedName = null;
        indexofSecretAchievement = -1;
        potentialSecretAchievements.clear();
        chosenHandCard = null;
        isSavedGame = false;
        clientConnectionHandler.clear();
        setGameState(GameState.LOBBY);
    }

    @Override
    public void clearUI() {
        ui.clear();
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }
}
