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

    /**
     * Constructor for the BoardInitMessage, which is used to initialize the board
     * @param resourceDeck
     * @param goldDeck
     * @param achievementDeck
     * @param players
     */
    public BoardInitMessage(ResourceDeck resourceDeck, GoldDeck goldDeck, AchievementDeck achievementDeck, List<Player> players) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.achievementDeck = achievementDeck;
        this.players = players;
    }

    /**
     * Constructor for the acknowledgement of the BoardInitMessage
     */

    public BoardInitMessage() {}

    /**
     * Executes the server side of the message
     * @param controller, which is ServerController
     */
    public void serverExecute(Controller controller) {
        controller.ackInitBoard();
    }

    /**
     * Executes the client side of the message,
     * @param controller, which is ClientController
     */
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
