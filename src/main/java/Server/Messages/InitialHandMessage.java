package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class InitialHandMessage implements Serializable, GeneralMessage {

    private Card card1, card2, card3;

    public InitialHandMessage(Card card1, Card card2, Card card3){
        this.card1= card1; this.card2 = card2; this.card3 = card3;
    }

    public InitialHandMessage() {}

    @Override
    public void serverExecute(Controller controller) {
        controller.ackInitHand();

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setInitialHand(card1, card2, card3);
        InitialHandMessage ack = new InitialHandMessage();
        try {
            controller.getClientConnectionHandler().sendMessage(ack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
