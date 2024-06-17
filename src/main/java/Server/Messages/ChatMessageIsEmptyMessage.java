package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * This message is used to inform the client that they cannot send an empty message to the chat
 */
public class ChatMessageIsEmptyMessage implements ToClientMessage, Serializable {
    @Override
    public void clientExecute(ClientController controller){
        controller.chatMessageIsEmpty();
    }
}
