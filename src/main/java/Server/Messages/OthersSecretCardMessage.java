package Server.Messages;

import Server.Card.Card;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;

public class OthersSecretCardMessage implements Serializable, GeneralMessage {
    private final Card card;
    private final Player player;

    public OthersSecretCardMessage(Card card, Player player){
        this.card = card;
        this.player = player;
    }


    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute() {

    }
}
