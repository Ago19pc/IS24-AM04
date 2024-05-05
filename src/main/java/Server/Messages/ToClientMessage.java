package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;

public interface ToClientMessage {
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException;
}
