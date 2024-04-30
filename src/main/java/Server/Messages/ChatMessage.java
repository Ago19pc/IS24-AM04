package Server.Messages;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Controller.Controller;
import Server.Enums.MessageType;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class ChatMessage implements Serializable, GeneralMessage {
    private final Message message;

    public ChatMessage(Message message){
        this.message = message;
    }



    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.addMessage(message.getMessage(), controller.getPlayerByName(message.getName()));
            controller.getConnectionHandler().getThreads().stream()
                    .forEach(c -> c.sendMessages(this));
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.addChatMessage(this.message);
        System.out.println("[" + message.getTimestamp() + "] " + message.getName() + ": " + message.getMessage());

    }
}
