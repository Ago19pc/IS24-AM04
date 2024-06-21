package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;

import java.io.Serializable;
import java.util.List;

/**
 * Message to send the client their initial hand
 */
public class InitialHandMessage implements Serializable, ToClientMessage {
    /**
     * The player hand
     */
    private final List<Card> hand;

    /**
     * Constructor
     * @param hand the hand to send
     */
    public InitialHandMessage(List<Card> hand){
        this.hand = hand;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.giveInitialHand(hand);
    }
}
