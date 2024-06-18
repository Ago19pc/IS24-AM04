package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.List;

/**
 * Message to inform the client of the order of the players
 */
public class PlayerOrderMessage implements Serializable, ToClientMessage {

    private final List<String> playerNames;
    public PlayerOrderMessage(List<String> playerNames) {
        this.playerNames = playerNames;
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.updatePlayerOrder(playerNames);
    }
}
