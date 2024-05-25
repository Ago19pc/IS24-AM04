package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class PlayerAlreadyPlayingMessage implements Serializable, ToClientMessage {
    public PlayerAlreadyPlayingMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.playerAlreadyPlaying();
    }
}
