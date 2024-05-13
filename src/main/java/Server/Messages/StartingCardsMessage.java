package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class StartingCardsMessage implements Serializable, ToClientMessage {

    private Card card;

    public StartingCardsMessage(Card card){
        this.card = card;
    }


    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.giveStartingCard(card);
    }
}

