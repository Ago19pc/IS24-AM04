package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class ChatMessageIsEmptyMessage implements ToClientMessage, Serializable {
    @Override
    public void clientExecute(ClientController controller){
        controller.chatMessageIsEmpty();
    }
}
