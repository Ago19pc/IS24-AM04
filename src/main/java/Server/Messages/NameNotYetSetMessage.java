package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that he needs to set his name before he can play
 */
public class NameNotYetSetMessage implements Serializable, ToClientMessage{
    /**
     * Constructor
     */
    public NameNotYetSetMessage() {
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.nameNotYetSet();
    }
}
