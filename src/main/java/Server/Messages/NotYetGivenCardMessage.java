package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;

import java.io.Serializable;

/**
 * Message to inform the client that he still needs to receive one or more cards to do what he wants
 */
public class NotYetGivenCardMessage implements Serializable, ToClientMessage {
    private final Actions type;
    /**
     * Constructor
     * @param type the type of action that needs a card
     */
    public NotYetGivenCardMessage(Actions type) {
        this.type = type;
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.notYetGivenCard(type);
    }
}
