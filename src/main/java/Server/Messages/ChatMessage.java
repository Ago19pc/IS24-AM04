package Server.Messages;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class ChatMessage implements Serializable, ToServerMessage, ToClientMessage {
    private String message;
    private String nameOrId;

    /**
     * Constructor for the ChatMessage, which is the message used to update the chat
     * @param message
     * @param player
     */
    public ChatMessage(String message, String player){
        this.message = message;
        this.nameOrId = player;
    }

    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.nameOrId);
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
        controller.addChatMessage(new Message(message, nameOrId));
    }
}
