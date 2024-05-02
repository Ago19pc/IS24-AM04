package Client.Controller;

import Client.Connection.ClientConnectionHandler;
import Client.View.CLI;
import Client.Connection.ClientConnectionHandlerSOCKET;
import Client.Connection.GeneralClientConnectionHandler;
import ConnectionUtils.MessagePacket;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.TooManyElementsException;
import Server.Messages.*;
import Server.Player.Player;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private GeneralClientConnectionHandler clientConnectionHandler;

    private CLI cli ;
    private PossibleCardPlacementSave move;

    private String activePlayerName;

    private int turn = 0;

    public void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you wish to connect with RMI? (Y/N)");
        String choice = scanner.nextLine();
        choice = choice.toUpperCase();
        if (choice.equals("Y")) {
            clientConnectionHandler = new GeneralClientConnectionHandler(this, true);
        } else {
            clientConnectionHandler = new GeneralClientConnectionHandler(this, false);
        }

        clientConnectionHandler.start();
        this.cli = cli;
    }

    /**
     * Sets the turn of the game
     * @param turn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Gets the name of the player
     * @return
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Sets the name of the ActivePlayer, whose turn it is
     * @param name the name of the ActivePlayer
     */
    public void setActivePlayerName(String name){
        this.activePlayerName = name;
    }

    /**
     * Gets the name of the ActivePlayer
     * @return the name of the ActivePlayer
     */
    public String getActivePlayerName(){
        return activePlayerName;
    }

    /**
     * Sends a message to ask if the proposed name is accpeted
     * @param name, the proposed name
     */
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
    /**
     * Sets the name of the player to the proposed name
     */
    public void setName(Boolean confirmation){
        if(confirmation){
            this.myName = this.proposedName;
            cli.nameChanged(myName);
        } else {
            cli.nameChangeFailed();
        }
    }

    /**
     * Sets the cards on the board
     * @param achievementDeck
     * @param goldDeck
     * @param resourceDeck
     */
    public void boardInit(AchievementDeck achievementDeck, GoldDeck goldDeck, ResourceDeck resourceDeck){
        this.goldDeck = goldDeck;
        this.achievementDeck = achievementDeck;
        this.resourceDeck = resourceDeck;
    }

    /**
     * @return the ClientConnectionHandler
     */
    public GeneralClientConnectionHandler getClientConnectionHandler() {
        return clientConnectionHandler;
    }

    /**
     * Gets the Player associated with the name
     * @param name the name of the Player
     * @return the Player
     * @throws PlayerNotFoundByNameException if player not found
     */
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.players){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    /**
     * Sends the message with the cardPlacement
     * @param hand_pos the position of the card in the hand
     * @param face the face of the card
     * @param x the xCoord where to place the card
     * @param y the yCoord where to place the card
     */
    public void askForCardPlacement(int hand_pos, Face face, int x, int y){
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

    }

    /**
     * Places the card on the manuscript
     */
    public void cardPlacement(){
        try {
            getPlayerByName(myName).getManuscript().addCard(move.getX(), move.getY(), move.getCardFace(), 0);
        } catch (PlayerNotFoundByNameException e) {
            System.out.println("Cannot find myself");
        }

    }

    /**
     * Sends the message to set it's status to ready
     */
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

    /**
     * Connects the server localhost with port 1234, useful for testing
     * @throws IOException
     */
    public void debugConnect() throws IOException {
        try {
            clientConnectionHandler.setSocket("localhost", 1234);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the ClientConnectionHandler with debug mode
     */
    public void mainDebug(){
        clientConnectionHandler = new GeneralClientConnectionHandler(this, false,true);
    }

    /**
     * Proposes a color to the server
     * @param color, the color chosen
     */
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

    /**
     * Adds a message to the chat
     * @param message, the message to add
     */
    public void addChatMessage(Message message){
        chat.addMessage(message);
        cli.chat(message);
    }

    /**
     * Sends a chat message to the server
     * @param message, the message to send
     */
    public void sendChatMessage (String message){
        if (myName == null) {
            cli.needName();
            return;
        }
        Message m = new Message(message, myName);
        ChatMessage chatMessage = new ChatMessage(m);
        clientConnectionHandler.sendMessage(chatMessage);

    }

    /**
     * Sets the initial hand of the player
     * @param card1, the first card
     * @param card2, the second card
     * @param card3, the third card
     */
    public void setInitialHand (Card card1, Card card2, Card card3){
        try {
            getPlayerByName(myName).addCardToHand(card1);
            getPlayerByName(myName).addCardToHand(card2);
            getPlayerByName(myName).addCardToHand(card3);
        } catch (PlayerNotFoundByNameException | TooManyElementsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the player list
     * @param p, the list of players
     */
    public void setPlayerList (List<Player> p) {
        System.out.println("Setting player list" + p.get(0));
        this.players = p;
        cli.playerListChanged();
    }

    /**
     * Gets the player list
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the Color of the player
     */
    public Color getMyColor(){
        return myColor;
    }

    /**
     * Updates the unavailable colors list
     * @param unavaiableColors, the list of all unavailable colors
     */
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

}
