package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that the player with the name he tried to reconnect to is already playing
 */
public class PlayerAlreadyPlayingMessage implements Serializable, ToClientMessage {
    public PlayerAlreadyPlayingMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.playerAlreadyPlaying();
    }
}
