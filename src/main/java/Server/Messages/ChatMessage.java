package Server.Messages;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.MessageType;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class ChatMessage implements Serializable, ToServerMessage, ToClientMessage {
    private String message;
    private String name;

    /**
     * Constructor for the ChatMessage, which is the message used to update the chat
     * @param message
     */
    public ChatMessage(String message){
        this.message = message;
    }

    public ChatMessage(String message, String player){
        this.message = message;
        this.name = player;
    }

    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            ClientHandler client = controller.getConnectionHandler().getThreads()
                    .stream().filter(c -> c.getReceiver().threadId() == Thread.currentThread().threadId()).toList().getFirst();
            playerName = controller.getConnectionHandler().getThreadName(client);
            Player player = controller.getPlayerByName(playerName);
            controller.addMessage(message, player);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to correct client based on ip
        } catch (IllegalArgumentException e) {
            ChatMessageIsEmptyMessage chatMessageIsEmptyMessage = new ChatMessageIsEmptyMessage();
            controller.getConnectionHandler().sendMessage(chatMessageIsEmptyMessage, playerName);
        }

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.addChatMessage(new Message(message, name));
    }
}
