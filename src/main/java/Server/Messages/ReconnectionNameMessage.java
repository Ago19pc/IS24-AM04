package Server.Messages;

import java.io.Serializable;

/**
 * Message to inform the client that a player has reconnected
 */
public class ReconnectionNameMessage implements ToClientMessage, Serializable {
    private final String name;
    /**
     * Constructor
     * @param name the name of the player who reconnected
     */
    public ReconnectionNameMessage(String name) {
        this.name = name;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.setName(name);
    }
}
