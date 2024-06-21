package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that another player has received his initial hand
 */
public class OtherPlayerInitialHandMessage implements Serializable, ToClientMessage {
    /**
     * The name of the player who received their initial hand
     */
    private final String name;
    /**
     * Constructor
     * @param name the name of the player who received the initial hand
     */
    public OtherPlayerInitialHandMessage(String name){
        this.name = name;
    }


    @Override
    public void clientExecute(ClientController controller){
        controller.giveOtherPlayerInitialHand(name);
    }
}
