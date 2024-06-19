package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.List;

/**
 * Message to inform the client of the order of the players
 */
public class PlayerOrderMessage implements Serializable, ToClientMessage {
    /**
     * The names of the players in order
     */
    private final List<String> playerNames;
    /**
     * Constructor
     * @param playerNames the names of the players in order
     */
    public PlayerOrderMessage(List<String> playerNames) {
        this.playerNames = playerNames;
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.updatePlayerOrder(playerNames);
    }
}
