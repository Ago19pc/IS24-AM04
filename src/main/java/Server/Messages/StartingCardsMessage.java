package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class StartingCardsMessage implements Serializable, ToClientMessage {

    private final Card card;

    public StartingCardsMessage(Card card){
        this.card = card;
    }


    @Override
    public void clientExecute(ClientController controller) {
        controller.giveStartingCard(card);
    }
}

