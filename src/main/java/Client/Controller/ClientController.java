package Client.Controller;

import Client.Connection.ClientConnectionHandler;
import Client.View.CLI;
import ConnectionUtils.ToServerMessagePacket;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Card.CornerCardFace;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.TooManyElementsException;
import Server.Messages.*;
import Server.Player.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientController {
    private  List<Player> players;
    private  String myName;
    private String proposedName;
    private Color myColor;
    private Color proposedColor;
    private List<Color> unavaiableColors = new ArrayList<>();
    private boolean myReady = false;
    private  AchievementDeck achievementDeck;
    private  GoldDeck goldDeck;
    private  ResourceDeck resourceDeck;
    private  Chat chat = new Chat();
    private  ClientConnectionHandler clientConnectionHandler;
    private CLI cli ;
    private PossibleCardPlacementSave move;

    private String activePlayerName;

    private int turn = 0;

    public void main(CLI cli) {
        clientConnectionHandler = new ClientConnectionHandler(this);
        clientConnectionHandler.start();
        this.cli = cli;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
    public String getMyName() {
        return myName;
    }

    public void setActivePlayerName(String name){
        this.activePlayerName = name;
    }

    public String getActivePlayerName(){
        return activePlayerName;
    }

    public void askSetName(String name) {
        this.proposedName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName, true);
        if (clientConnectionHandler.sender.getOutputBuffer() == null){
            cli.needConnection();
            return;
        }
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            e.printStackTrace();
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
    public void boardInit(AchievementDeck achievementDeck, GoldDeck goldDeck, ResourceDeck resourceDeck){
        this.goldDeck = goldDeck;
        this.achievementDeck = achievementDeck;
        this.resourceDeck = resourceDeck;
    }

    public ClientConnectionHandler getClientConnectionHandler() {
        return clientConnectionHandler;
    }

    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }
    /*public void askForCardPlacement(int hand_pos, Face face, int x, int y){
        CornerCardFace cardFace = null;
        try {
            cardFace = (CornerCardFace) getPlayerByName(myName).getHand().get(hand_pos).getFace(face);
        } catch (PlayerNotFoundByNameException e) {
            System.out.println("Cannot find myself");
        }
        move = new PossibleCardPlacementSave(x, y, cardFace);
        CardPlacementMessage cardPlacementMessage = new CardPlacementMessage(myName, hand_pos, x, y, face);
        try {
            clientConnectionHandler.sendMessage(cardPlacementMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public void cardPlacement(){
        try {
            getPlayerByName(myName).getManuscript().addCard(move.getX(), move.getY(), move.getCardFace(), 0);
        } catch (PlayerNotFoundByNameException e) {
            System.out.println("Cannot find myself");
        }

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
        public void debugConnect() throws IOException {

        clientConnectionHandler.setSocket(new Socket("localhost", 1234));

        }

        public void mainDebug(){

        clientConnectionHandler = new ClientConnectionHandler(true, this);

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

    public void setColor(boolean confirmation, Color color){
        if(confirmation){
            this.myColor = color;
            cli.colorChanged();
        } else {
            cli.colorChangeFailed();
        }
    }
    public void addChatMessage(Message message){
        chat.addMessage(message);
        cli.chat(message);
    }

    public void sendChatMessage (String message){
        if (myName == null) {
            cli.needName();
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message);
        ToServerMessagePacket mp = new ToServerMessagePacket(chatMessage);
        try {
            clientConnectionHandler.sender.sendMessage(mp.stringify());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInitialHand (Card card1, Card card2, Card card3){
        try {
            getPlayerByName(myName).addCardToHand(card1);
            getPlayerByName(myName).addCardToHand(card2);
            getPlayerByName(myName).addCardToHand(card3);
        } catch (PlayerNotFoundByNameException | TooManyElementsException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerList (List<Player> p) {
        this.players = p;
        cli.playerListChanged();
    }
    public List<Player> getPlayers() {
        return players;
    }

    public Color getMyColor(){
        return myColor;
    }

    public void setUnavaiableColors(List<Color> unavaiableColors){
        this.unavaiableColors = unavaiableColors;
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

    public void giveAchievementCards(List<Card> secretCards, List<Card> commonCards) {
    }

    public void achievementDeckDrawInvalid() {
    }

    public void alreadyDone(Actions action) {
    }

    public void cardNotPlaceable() {
    }

    public void chatMessageIsEmpty() {
    }

    public void colorNotYetSet() {
    }

    public void giveDrawnCard(Card drawnCard) {
    }

    public void emptyDeck() {
    }

    public void setEndGame() {
    }

    public void gameAlreadyStarted() {
    }

    public void gameNotYetStarted() {
    }

    public void giveInitialHand(List<Card> hand) {
    }

    public void invalidCard(Actions cardType) {
    }

    public void displayLeaderboard(Map<String, Integer> playerPoints) {
    }

    public void nameNotYetSet() {
    }

    public void newPlayer(List<String> playerNames) {
    }

    public void notYetGivenCard(Actions type) {
    }

    public void notYourTurn() {
    }

    public void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards, int turnNumber, String activePlayerName) {
    }

    public void giveOtherPlayerInitialHand(String name) {
    }

    public void setSecretCard(String name) {
    }

    public void startingCardChosen(String name, CardFace startingFace) {
    }

    public void startGame(List<String> playerNames, List<Card> goldBoardCards, List<Card> resourceBoardCards) {
    }

    public void giveStartingCard(Card card) {
    }

    public void toDoFirst(Actions actionToDo) {

    }
}
