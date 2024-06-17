package Server.Messages;

import Client.Controller.ClientController;

public interface ToClientMessage {
    /**
     * This method is used to execute the message on the client side
     * @param controller the controller where the message will be executed
     */
    void clientExecute(ClientController controller);
}
