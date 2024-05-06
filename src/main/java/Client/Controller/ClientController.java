package Client.Controller;

import Client.Connection.ClientConnectionHandler;
import Client.Connection.ClientConnectionHandlerRMI;
import Client.Connection.GeneralClientConnectionHandler;
import Client.Deck;
import Client.View.CLI;
import ConnectionUtils.ToServerMessagePacket;
import Server.Card.*;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.*;
import Client.Player;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    private GeneralClientConnectionHandler clientConnectionHandler;
    private CLI cli ;
    private boolean rmiMode = false;

    //cards not owned by player info
    private List<AchievementCard> commonAchievements;
    private Deck goldDeck;
    private Deck resourceDeck;

    //player info
    private String myName;
    private Color myColor;
    private boolean myReady = false;
    private AchievementCard secretAchievement;
    private List<ResourceCard> hand = new ArrayList<>();

    //other game info
    private int turn = 0;
    private List<Color> unavaiableColors = new ArrayList<>();
    private Chat chat = new Chat();
    private List<Player> players = new ArrayList<>();

    //temp stuff
    private String proposedName;
    private Color proposedColor;
    private AchievementCard proposedSecretAchievement;

    public void main(CLI cli) throws RemoteException {
        cli.askConnectionMode();
        clientConnectionHandler = new GeneralClientConnectionHandler(this, rmiMode);
        this.cli = cli;
    }

    //helper methods
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    //ui actions
    public void joinServer(String ip, int port) {
        try{
            clientConnectionHandler.setSocket(ip, port);
        } catch (IOException | NotBoundException e){
            cli.connectionFailed();
        }
        cli.successfulConnection();
    }
    public void setReady() {
        if (myName == null || myColor == null) {
            cli.needNameOrColor();
            return;
        }
        this.myReady = true;
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, myName);
        try {
            clientConnectionHandler.sendMessage(readyStatusMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void askSetColor(String color) {
        if (this.myName == null) {
            cli.needName();
            return;
        }
        Color castedColor;
        try {
            castedColor = Color.valueOf(color.toUpperCase());
            if (unavaiableColors.contains(castedColor)){
                cli.unavailableColor();
                return;
            }
        } catch (IllegalArgumentException e) {
            cli.invalidColor();
            return;
        }
        proposedColor = castedColor;
        PlayerColorMessage playerColorMessage = new PlayerColorMessage(true, myName, castedColor, true);
        try {
            clientConnectionHandler.sendMessage(playerColorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendChatMessage (String message){
        if (myName == null) {
            cli.needName();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message);
        clientConnectionHandler.sendMessage(chatMessage);
    }
    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, true);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            cli.needConnection();
        }
    }
    public void setRMIMode(boolean rmi) {
        this.rmiMode = rmi;
    }

    //ui getters
    public String getMyName() {
        return myName;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public Color getMyColor(){
        return myColor;
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
                boardCards = goldDeck.getBoardCards();
            case RESOURCE:
                boardCards = resourceDeck.getBoardCards();
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
    public void loadLobbyInfo(List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady) {
        for (String name : playerNames){
            Player p = new Player(name);
            p.setColor(playerColors.get(name));
            p.setReady(playerReady.get(name));
            players.add(p);
        }
        cli.displayLobby();
    }
    public void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards) {
        commonAchievements = new ArrayList<>();
        for (Card c : commonCards){
            commonAchievements.add((AchievementCard) c);
        }
        cli.displayCommonAchievements();
        cli.chooseSecretAchievement(secretCards);
    }
    public void achievementDeckDrawInvalid() {
        cli.cantDrawAchievementCards();
    }
    public void alreadyDone(Actions action) {
        cli.alreadyDone(action);
    }
    public void cardNotPlaceable() {
        cli.cardNotPlaceable();
    }
    public void addChatMessage(Message message){
        chat.addMessage(message);
        cli.chat(message);
    }
    public void chatMessageIsEmpty() {
        cli.chatMessageIsEmpty();
    }
    public void colorNotYetSet() {
        cli.needColor();
    }
    public void giveDrawnCard(Card drawnCard) {
        hand.add((ResourceCard) drawnCard);
        cli.displayNewCardInHand();
    }
    public void emptyDeck() {
        cli.deckIsEmpty();
    }
    public void setEndGame() {
        cli.endGameStarted();
    }
    public void gameAlreadyStarted() {
        cli.gameAlreadyStarted();
    }
    public void gameNotYetStarted() {
        cli.gameNotYetStarted();
    }
    public void giveInitialHand(List<Card> hand) {
        for (Card c : hand){
            this.hand.add((ResourceCard) c);
        }
        cli.displayInitialHand();
    }
    public void invalidCard(Actions cardType) {
        cli.invalidCardForAction(cardType);
    }
    public void displayLeaderboard(Map<String, Integer> playerPoints) {
        for (String name : playerPoints.keySet()){
            try {
                getPlayerByName(name).setAchievementPoints(playerPoints.get(name) - getPlayerByName(name).getPoints());
            } catch (PlayerNotFoundByNameException e) {
                throw new RuntimeException(e);
            }
        }
        cli.displayLeaderboard();
    }
    public void nameNotYetSet() {
        cli.needName();
    }
    public void newPlayer(List<String> playerNames) {
        Player newPlayer = new Player(playerNames.getLast());
        players.add(newPlayer);
        cli.displayNewPlayer();
    }
    public void notYetGivenCard(Actions type) {
        cli.notYetGivenCard(type);
    }
    public void notYourTurn() {
        cli.notYourTurn();
    }
    public void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards, int turnNumber, String activePlayerName) throws PlayerNotFoundByNameException {
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
        cli.otherPlayerDraw(name, deckFrom, drawPosition);
        turn = turnNumber;
        for(Player p : players){
            p.setActive(p.getName().equals(activePlayerName));
        }
        cli.newTurn();
    }
    public void giveOtherPlayerInitialHand(String name) throws PlayerNotFoundByNameException {
        Player p = getPlayerByName(name);
        p.setHandSize(3);
        resourceDeck.setDeckSize(resourceDeck.getDeckSize() - 2);
        goldDeck.setDeckSize(goldDeck.getDeckSize() - 1);
        cli.otherPlayerInitialHand(name);
    }
    public void setColor(boolean confirmation, Color color){
        if(confirmation){
            this.myColor = color;
            cli.colorChanged();
        } else {
            cli.colorChangeFailed();
        }
    }
    /**
     * Updates the player with the new color and tells the user which players have which colors
     */
    public void updatePlayerColors(Color color, String name) {
        try {
            getPlayerByName(name).setColor(color);
            cli.displayPlayerColors();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
    public void setName(Boolean confirmation){
        if(confirmation){
            this.myName = this.proposedName;
            cli.nameChanged(myName);
        } else {
            cli.nameChangeFailed();
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
            cli.updateReady(name, ready);
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSecretCard(String name) {
        if (name.equals(myName)){
            secretAchievement = proposedSecretAchievement;
        }
        cli.secretAchievementChosen(name);
    }
    public void startingCardChosen(String name, CardFace startingFace) {
        if (name.equals(myName)){
            //todo: initialize manuscript
        }
        cli.startingCardChosen(name);
    }
    public void startGame(List<Card> goldBoardCards, List<Card> resourceBoardCards){
        cli.gameStarted();
        goldDeck = new Deck(goldBoardCards);
        resourceDeck = new Deck(resourceBoardCards);
        cli.displayBoardCards();
    }
    public void updatePlayerOrder(List<String> playerNames) throws PlayerNotFoundByNameException {
        List<Player> newPlayerList = new ArrayList<>();
        for (String name : playerNames){
            newPlayerList.add(getPlayerByName(name));
        }
        players = newPlayerList;
        cli.displayPlayerOrder();
    }
    public void giveStartingCard(Card card) {
        cli.chooseStartingCardFace(card);
    }
    public void toDoFirst(Actions actionToDo) {
        cli.doFirst(actionToDo);
    }
}
