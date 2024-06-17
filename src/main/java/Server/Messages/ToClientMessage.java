package Server.Messages;

import Client.Controller.ClientController;

/**
 * This interface is used to define the execution of a message that is sent to the clients
 */
public interface ToClientMessage {
    /**
     * This method is used to execute the message on the client side
     * @param controller the controller where the message will be executed
     */
    public void clientExecute(ClientController controller);
}
