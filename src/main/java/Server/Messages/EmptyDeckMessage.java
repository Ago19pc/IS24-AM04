package Server.Messages;

import java.io.Serializable;

/**
 * Message to inform the client that the deck he is trying to draw from, in the position he is trying to draw from, is empty

 */
public class EmptyDeckMessage implements Serializable, ToClientMessage {
    public EmptyDeckMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.emptyDeck();
    }
}
