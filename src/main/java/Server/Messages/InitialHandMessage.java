package Server.Messages;

import Server.Card.Card;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class InitialHandMessage implements Serializable, GeneralMessage {

    private final Map<Player, List<Card>> cards;

    public InitialHandMessage(Map<Player, List<Card>> cards){
        this.cards = cards;
    }


    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute() {

    }
}
