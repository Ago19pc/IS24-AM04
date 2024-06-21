package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that the name he tried to use has already been taken
 */
public class InvalidNameMessage implements ToClientMessage, Serializable {
    /**
     * Constructor
     */
    public InvalidNameMessage() {}

    @Override
    public void clientExecute(ClientController controller){
        controller.invalidName();
    }
}
