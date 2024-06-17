package Server.Messages;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Player.Player;

import java.io.Serializable;

public class ChatMessage implements Serializable, ToServerMessage, ToClientMessage {
    private final String message;
    private final String nameOrId;

    /**
     * Constructor for the ChatMessage, which is the message used to update the chat
     * @param message the message to be sent
     * @param player the player who sent the message
     */
    public ChatMessage(String message, String player){
        this.message = message;
        this.nameOrId = player;
    }

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
        }
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.addChatMessage(new Message(message, nameOrId));
    }
}
