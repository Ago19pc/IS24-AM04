package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;

import java.io.Serializable;
import java.util.List;

/**
 * Message to send the client their initial hand
 */
public class InitialHandMessage implements Serializable, ToClientMessage {

    private final List<Card> hand;

    public InitialHandMessage(List<Card> hand){
        this.hand = hand;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.giveInitialHand(hand);
    }
}
