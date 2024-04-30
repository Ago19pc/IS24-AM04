package Client.Controller;

import Client.Connection.ClientConnectionHandler;
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
import java.util.ArrayList;
import java.util.List;

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

    private PossibleCardPlacementSave move;

    private String activePlayerName;

    private int turn = 0;

    public void main() {
        clientConnectionHandler = new ClientConnectionHandler(this);
        clientConnectionHandler.start();

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
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the name of the player to the proposed name
     */
    public void setName(){
        this.myName = this.proposedName;
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
    public ClientConnectionHandler getClientConnectionHandler() {
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
            System.out.println("You must set your name and color first");
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
        clientConnectionHandler.setSocket(new Socket("localhost", 1234));
    }

    /**
     * Creates the ClientConnectionHandler with debug mode
     */
    public void mainDebug(){
        clientConnectionHandler = new ClientConnectionHandler(true, this);
    }

    /**
     * Proposes a color to the server
     * @param color, the color chosen
     */
    public void askSetColor(String color) {
        if (this.myName == null) {
            System.out.println("You must set your name first");
            return;
        }
        Color castedColor;
        try {
            castedColor = Color.valueOf(color.toUpperCase());
            if (unavaiableColors.contains(castedColor)){
                System.out.println("Color not avaiable, choose one from the following: ");
                for(Color c : Color.values())
                    if (!unavaiableColors.contains(c))
                        System.out.println(c);
                return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color, must be RED, YELLOW, BLUE or GREEN");
            return;
        }
        proposedColor = castedColor;
        PlayerColorMessage playerColorMessage = new PlayerColorMessage(myName, castedColor);
        try {
            clientConnectionHandler.sendMessage(playerColorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the color of the player
     */
    public void setColor(){
        myColor = proposedColor;
    }

    /**
     * Adds a message to the chat
     * @param message, the message to add
     */
    public void addChatMessage(Message message){
        chat.addMessage(message);
    }

    /**
     * Sends a chat message to the server
     * @param message, the message to send
     */
    public void sendChatMessage (String message){
        if (myName == null) {System.out.println("You need to set your name first"); return;}
        Message m = new Message(message, myName);
        ChatMessage chatMessage = new ChatMessage(m);
        MessagePacket mp = new MessagePacket(chatMessage);
        try {
            clientConnectionHandler.sender.sendMessage(mp.stringify());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


}
