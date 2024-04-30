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
import Server.Enums.MessageType;
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
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(proposedName);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setName(){
        this.myName = this.proposedName;
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

    public void cardPlacement(){
        try {
            getPlayerByName(myName).getManuscript().addCard(move.getX(), move.getY(), move.getCardFace(), 0);
        } catch (PlayerNotFoundByNameException e) {
            System.out.println("Cannot find myself");
        }

    }

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
        public void debugConnect() throws IOException {

        clientConnectionHandler.setSocket(new Socket("localhost", 1234));

        }

        public void mainDebug(){

        clientConnectionHandler = new ClientConnectionHandler(true, this);

        }
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

    public void setColor(){
        myColor = proposedColor;
    }
    public void addChatMessage(Message message){
        chat.addMessage(message);
    }

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
        System.out.println("Setting player list" + p.get(0));
        this.players = p;
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


}
