package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
