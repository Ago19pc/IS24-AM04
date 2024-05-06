package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

public interface ToClientMessage {
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException, PlayerNotFoundByNameException;
}
