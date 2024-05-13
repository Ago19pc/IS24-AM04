package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class ChatMessageIsEmptyMessage implements ToClientMessage, Serializable {
    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.chatMessageIsEmpty();
    }
}
