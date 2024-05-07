package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

public interface ToClientMessage {
    /**
     * This method is used to execute the message on the client side
     * @param controller the controller where the message will be executed
     * @throws ClientExecuteNotCallableException if the message can't be executed
     * @throws PlayerNotFoundByNameException if the player is not found by name
     */
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException, PlayerNotFoundByNameException;
}
