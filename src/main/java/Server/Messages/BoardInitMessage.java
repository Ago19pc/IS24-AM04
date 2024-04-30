package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class BoardInitMessage implements Serializable, GeneralMessage {

    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;

    private AchievementDeck achievementDeck;
    private List<Player> players;


    public BoardInitMessage(ResourceDeck resourceDeck, GoldDeck goldDeck, AchievementDeck achievementDeck, List<Player> players) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.achievementDeck = achievementDeck;
        this.players = players;
    }

    public BoardInitMessage() {}

    public void serverExecute(Controller controller) {
        controller.ackInitBoard();
    }
    public void clientExecute(ClientController controller){

        controller.boardInit(achievementDeck, goldDeck, resourceDeck);
        controller.setPlayerList(players);
        BoardInitMessage ack = new BoardInitMessage();
        try {
            controller.getClientConnectionHandler().sendMessage(ack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
