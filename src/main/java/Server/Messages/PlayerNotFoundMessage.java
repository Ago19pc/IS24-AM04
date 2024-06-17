package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that a player does not exist. Called when the client tries to reconnect to an id whose player does not exist
 */
public class PlayerNotFoundMessage implements Serializable, ToClientMessage {
    public PlayerNotFoundMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.idNotInGame();
    }
}
