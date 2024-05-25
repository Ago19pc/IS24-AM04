package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class PlayerNotFoundMessage implements Serializable, ToClientMessage {
    public PlayerNotFoundMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.idNotInGame();
    }
}
