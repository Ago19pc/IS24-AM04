package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that a player has disconnected
 */
public class PlayerDisconnectedMessage implements Serializable, ToClientMessage{
    private final String playerName;

    public PlayerDisconnectedMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.playerDisconnected(playerName);
    }
}
