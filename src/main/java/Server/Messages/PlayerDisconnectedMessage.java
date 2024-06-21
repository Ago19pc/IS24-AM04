package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that a player has disconnected
 */
public class PlayerDisconnectedMessage implements Serializable, ToClientMessage{
    /**
     * The name of the player who disconnected
     */
    private final String playerName;
    /**
     * Constructor
     * @param playerName the name of the player who disconnected
     */
    public PlayerDisconnectedMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.playerDisconnected(playerName);
    }
}
