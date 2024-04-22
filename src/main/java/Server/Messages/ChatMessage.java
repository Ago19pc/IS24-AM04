package Server.Messages;

import Server.Chat.Message;
import Server.Controller.Controller;
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
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clientExecute() {

    }
}
