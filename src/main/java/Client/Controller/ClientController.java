package Client.Controller;

import Client.Connection.ClientConnectionHandler;
import Server.Chat.Chat;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Messages.PlayerNameMessage;
import Server.Messages.ReadyStatusMessage;
import Server.Player.Player;

import java.util.List;

public class ClientController {
    private  List<Player> players;
    private  String myName;
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
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, myName);
        try {
            clientConnectionHandler.sendMessage(readyStatusMessage, Server.Enums.MessageType.READYSTATUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
