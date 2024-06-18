package Server.Messages;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Player.Player;

import java.io.Serializable;

/**
 * This message is used to send a message to the chat and update the clients
 */
public class ChatMessage implements Serializable, ToServerMessage, ToClientMessage {
    private final String message;
    private final String nameOrId;

    public ChatMessage(String message, String player){
        this.message = message;
        this.nameOrId = player;
    }

    /**
     * Sends a message to the chat
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller) {
        String playerName;
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.nameOrId);
            Player player = controller.getPlayerByName(playerName);
            controller.addMessage(message, player);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(nameOrId).sendMessage(message, nameOrId);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (java.rmi.RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (IllegalArgumentException e) {
            ChatMessageIsEmptyMessage chatMessageIsEmptyMessage = new ChatMessageIsEmptyMessage();
            controller.getConnectionHandler().sendMessage(chatMessageIsEmptyMessage, playerName);
        }

    }

    /**
     * Updates the clients with the new chat message
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        controller.addChatMessage(new Message(message, nameOrId));
    }
}
