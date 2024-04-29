package Client.Controller;

import Client.Connection.ClientConnectionHandler;
import Server.Chat.Chat;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.Color;
import Server.Messages.PlayerColorMessage;
import Server.Messages.PlayerNameMessage;
import Server.Messages.ReadyStatusMessage;
import Server.Player.Player;

import java.util.List;

public class ClientController {
    private  List<Player> players;
    private  String myName;
    private Color myColor;
    private boolean myReady = false;
    private  AchievementDeck achievementDeck;
    private  GoldDeck goldDeck;
    private  ResourceDeck resourceDeck;
    private  Chat chat;
    private  ClientConnectionHandler clientConnectionHandler;

    public void main() {
        clientConnectionHandler = new ClientConnectionHandler(this);
        clientConnectionHandler.start();

    }

    public void setName(String name) {
        myName = name;
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(myName);
        try {
            clientConnectionHandler.sendMessage(playerNameMessage, Server.Enums.MessageType.PLAYERNAME);
        } catch (Exception e) {
            e.printStackTrace();
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
            clientConnectionHandler.sendMessage(readyStatusMessage, Server.Enums.MessageType.READYSTATUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColor(String color) {
        if (myName == null) {
            System.out.println("You must set your name first");
            return;
        }
        Color castedColor;
        try {
            castedColor = Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color, must be RED, YELLOW, BLUE or GREEN");
            return;
        }
        myColor = castedColor;
        PlayerColorMessage playerColorMessage = new PlayerColorMessage(myName, castedColor);
        try {
            clientConnectionHandler.sendMessage(playerColorMessage, Server.Enums.MessageType.PLAYERCOLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
