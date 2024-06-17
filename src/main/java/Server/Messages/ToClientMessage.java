package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

public interface ToClientMessage {
    /**
     * This method is used to execute the message on the client side
     * @param controller the controller where the message will be executed
     */
    void clientExecute(ClientController controller);
}
