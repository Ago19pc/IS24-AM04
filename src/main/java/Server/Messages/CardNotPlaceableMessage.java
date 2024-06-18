package Server.Messages;

import java.io.Serializable;

/**
 * This message is used to inform the client that the card placement is invalid
 */
public class CardNotPlaceableMessage implements Serializable, ToClientMessage {
    public CardNotPlaceableMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.cardNotPlaceable();
    }
}
