package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;

import java.io.Serializable;

/**
 * Message to send the client his starting card
 */
public class StartingCardsMessage implements Serializable, ToClientMessage {

    private final Card card;
    /**
     * Constructor
     * @param card the card
     */
    public StartingCardsMessage(Card card){
        this.card = card;
    }


    @Override
    public void clientExecute(ClientController controller) {
        controller.giveStartingCard(card);
    }
}

