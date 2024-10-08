package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that another player has reconnected
 */
public class OtherPlayerReconnectionMessage implements Serializable, ToClientMessage {
    /**
     * The name of the player who reconnected
     */
    private final String name;
    /**
     * Constructor
     * @param name the name of the player who reconnected
     */
    public OtherPlayerReconnectionMessage(String name){
        this.name = name;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.otherPlayerReconnected(name);
    }
}
