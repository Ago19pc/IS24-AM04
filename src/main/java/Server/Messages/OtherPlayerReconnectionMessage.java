package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that another player has reconnected
 */
public class OtherPlayerReconnectionMessage implements Serializable, ToClientMessage {
private final String name;
    public OtherPlayerReconnectionMessage(String name){
        this.name = name;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.otherPlayerReconnected(name);
    }
}
