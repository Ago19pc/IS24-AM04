package Server.Messages;

import java.io.Serializable;

public class EmptyDeckMessage implements Serializable, ToClientMessage {
    public EmptyDeckMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.emptyDeck();
    }
}
