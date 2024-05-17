package Client.Controller;

import Client.Connection.GeneralClientConnectionHandler;
import Client.Deck;
import Client.Player;
import Client.View.UI;
import Server.Card.*;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private List<ResourceCard> hand = new ArrayList<>();

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
    private AchievementCard proposedSecretAchievement;

    public void main(UI ui) throws RemoteException {
        clientConnectionHandler = new GeneralClientConnectionHandler(this, rmiMode);
        this.ui = ui;
        this.gameState = GameState.LOBBY;
    }

    //helper methods
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }
    public Color getMyColor() {
        Color myColor = null;
        try {
            myColor = getPlayerByName(myName).getColor();
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
        return myColor;
    }
    public void setMyColor(Color color) {
        try {
            getPlayerByName(myName).setColor(color);
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
    }
    public void setId(String id) {
        this.id = id;
    }

    //ui actions
    public void joinServer(String ip, int port) {
        System.out.println("Joining server");
        try{
            clientConnectionHandler.setSocket(ip, port);
            ui.successfulConnection();
        } catch (IOException | NotBoundException e){
            e.printStackTrace();
            ui.connectionFailed();
        } catch (ClientExecuteNotCallableException e) {
            throw new RuntimeException(e);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
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
    public void sendChatMessage (String message){
        if (myName == null) {
            ui.needName();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message, id);
        clientConnectionHandler.sendMessage(chatMessage);
    }
    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, true, id);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            ui.nameChangeFailed();
        }
    }
    public void setRMIMode(boolean rmi) {
        this.rmiMode = rmi;
    }
    public void chooseStartingCardFace(Face face) {
        SetStartingCardMessage startingCardMessage = new SetStartingCardMessage(face, id);
        try {
            clientConnectionHandler.sendMessage(startingCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void chooseSecretAchievement(AchievementCard secretAchievement, int index) {
        this.proposedSecretAchievement = secretAchievement;
        SetSecretCardMessage secretAchievementMessage = new SetSecretCardMessage(index, id);
        try {
            clientConnectionHandler.sendMessage(secretAchievementMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void askPlayCard(int cardNumber, Face face, int x, int y) {
        PlayCardMessage placeCardMessage = new PlayCardMessage(id, cardNumber, face, x, y);
        try {
            clientConnectionHandler.sendMessage(placeCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void askDrawCard(Decks deck, DeckPosition deckPosition) {
        DrawCardMessage drawCardMessage = new DrawCardMessage(deckPosition, deck, id);
        try {
            clientConnectionHandler.sendMessage(drawCardMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ui getters
    public String getMyName() {
        return myName;
    }
    public List<Player> getPlayers() {
        return players;
    }
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
    public List<ResourceCard> getHand() {
        return hand;
    }
    public List<AchievementCard> getCommonAchievements() {
        return commonAchievements;
    }
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
    public int getDeckSize(Decks deck){
        int size = 0;
        switch (deck){
            case GOLD:
                size = goldDeck.getDeckSize();
            case RESOURCE:
                size = resourceDeck.getDeckSize();
        }
        return size;
    }
    public List<Card> getBoardCards(Decks deck){
        List<Card> boardCards = new ArrayList<>();
        switch (deck){
            case GOLD:
                boardCards.add(goldDeck.getBoardCards().getFirst());
                boardCards.add(goldDeck.getBoardCards().getLast());
            case RESOURCE:
                boardCards.add(resourceDeck.getBoardCards().getFirst());
                boardCards.add(resourceDeck.getBoardCards().getLast());
        }
        return boardCards;
    }
    public Player getActivePlayer(){
        for (Player p : players){
            if (p.isActive()) return p;
        }
        return null;
    }
    public int getTurn() {
        return turn;
    }
    public List<String> getPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        for (Player p : players){
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    //methods called by incoming messages
    public void loadLobbyInfo(String id, List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady) {
        setId(id);
        for (String name : playerNames){
            Player p = new Player(name);
            p.setColor(playerColors.get(name));
            p.setReady(playerReady.get(name));
            players.add(p);
        }
        ui.displayLobby();
    }
    public void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards) {
        gameState = GameState.CHOOSE_SECRET_ACHIEVEMENT;
        commonAchievements = new ArrayList<>();
        for (Card c : commonCards){
            commonAchievements.add((AchievementCard) c);
        }
        ui.displayCommonAchievements();
        ui.chooseSecretAchievement(secretCards);
    }
    public void achievementDeckDrawInvalid() {
        ui.cantDrawAchievementCards();
    }
    public void alreadyDone(Actions action) {
        ui.alreadyDone(action);
    }
    public void cardNotPlaceable() {
        ui.cardNotPlaceable();
    }
    public void addChatMessage(Message message){
        chat.addMessage(message);
        ui.chat(message);
    }
    public void chatMessageIsEmpty() {
        ui.chatMessageIsEmpty();
    }
    public void colorNotYetSet() {
        ui.needColor();
    }
    public void giveDrawnCard(Card drawnCard) {
        hand.add((ResourceCard) drawnCard);
        ui.displayNewCardInHand();
    }
    public void emptyDeck() {
        ui.deckIsEmpty();
    }
    public void setEndGame() {
        ui.endGameStarted();
    }
    public void gameAlreadyStarted() {
        ui.gameAlreadyStarted();
    }
    public void gameNotYetStarted() {
        ui.gameNotYetStarted();
    }
    public void giveInitialHand(List<Card> hand) {
        for (Card c : hand){
            this.hand.add((ResourceCard) c);
        }
        ui.displayHand();
    }
    public void invalidCard(Actions cardType) {
        ui.invalidCardForAction(cardType);
    }
    public void displayLeaderboard(Map<String, Integer> playerPoints) {
        gameState = GameState.LEADERBOARD;
        for (String name : playerPoints.keySet()){
            try {
                getPlayerByName(name).setAchievementPoints(playerPoints.get(name) - getPlayerByName(name).getPoints());
            } catch (PlayerNotFoundByNameException e) {
                throw new RuntimeException(e);
            }
        }
        ui.displayLeaderboard();
    }
    public void nameNotYetSet() {
        ui.needName();
    }
    public void newPlayer(List<String> playerNames) {
        Player newPlayer = new Player(playerNames.getLast());
        players.add(newPlayer);
        ui.displayNewPlayer();
    }
    public void notYetGivenCard(Actions type) {
        ui.notYetGivenCard(type);
    }
    public void notYourTurn() {
        ui.notYourTurn();
    }
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
    public void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards) throws PlayerNotFoundByNameException {
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
        getPlayerByName(name).setHandSize(getPlayerByName(name).getHandSize() + 1);
        ui.otherPlayerDraw(name, deckFrom, drawPosition);
    }
    public void giveOtherPlayerInitialHand(String name) throws PlayerNotFoundByNameException {
        Player p = getPlayerByName(name);
        p.setHandSize(3);
        resourceDeck.setDeckSize(resourceDeck.getDeckSize() - 2);
        goldDeck.setDeckSize(goldDeck.getDeckSize() - 1);
        ui.otherPlayerInitialHand(name);
    }
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
    public void updatePlayerColors(Color color, String name) {
        try {
            getPlayerByName(name).setColor(color);
            ui.displayPlayerColors();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
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
    public void setSecretCard(String name) {
        if (name.equals(myName)){
            secretAchievement = proposedSecretAchievement;
        }
        ui.secretAchievementChosen(name);
    }
    public void startingCardChosen(String name, CornerCardFace startingFace) throws PlayerNotFoundByNameException {
        getPlayerByName(name).initializeManuscript(startingFace);
        ui.startingCardChosen(name);
    }
    public void startGame(List<Card> goldBoardCards, List<Card> resourceBoardCards){
        ui.gameStarted();
        goldDeck = new Deck(goldBoardCards);
        resourceDeck = new Deck(resourceBoardCards);
        ui.displayBoardCards();
    }
    public void updatePlayerOrder(List<String> playerNames) throws PlayerNotFoundByNameException {
        List<Player> newPlayerList = new ArrayList<>();
        for (String name : playerNames){
            newPlayerList.add(getPlayerByName(name));
        }
        players = newPlayerList;
        gameState = GameState.PLACE_CARD;
        ui.displayPlayerOrder();
    }
    public void giveStartingCard(Card card) {
        gameState = GameState.CHOOSE_STARTING_CARD;
        ui.chooseStartingCardFace(card);
    }
    public void toDoFirst(Actions actionToDo) {
        ui.doFirst(actionToDo);
    }
    public void placeCard(String playerName, CornerCardFace placedCardFace, int x, int y, int points) throws PlayerNotFoundByNameException {
        getPlayerByName(playerName).addManuscriptPoints(points);
        getPlayerByName(playerName).addCardToManuscript(x, y, placedCardFace, turn);
        getPlayerByName(playerName).setHandSize(getPlayerByName(playerName).getHandSize() - 1);
        ui.cardPlaced(playerName, x, y);
        if(points > 0){
            ui.displayPlayerPoints(playerName);
        }
        gameState = GameState.DRAW_CARD;
    }

    public void playerDisconnected(String playerName) {
        players.removeIf(p -> p.getName().equals(playerName));
        ui.playerDisconnected(playerName);
    }
}
