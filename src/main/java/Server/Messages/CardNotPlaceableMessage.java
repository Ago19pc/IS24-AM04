package Server.Messages;

import java.io.Serializable;

public class CardNotPlaceableMessage implements Serializable, ToClientMessage {
    public CardNotPlaceableMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.cardNotPlaceable();
    }
}
