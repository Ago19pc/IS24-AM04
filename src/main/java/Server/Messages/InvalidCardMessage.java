package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;

import java.io.Serializable;

/**
 * Message to inform the client that the card he has selected is invalid. This is used when the player has selected a secret achievement card that was not given to him
 */
public class InvalidCardMessage implements ToClientMessage, Serializable {
    private final Actions cardType;

    public InvalidCardMessage(Actions cardType) {
        this.cardType = cardType;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.invalidCard(cardType);
    }
}
