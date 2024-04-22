package Server.Messages;

import Server.Card.CardFace;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;

public class OthersStartingCardMessage implements Serializable, GeneralMessage {

    private final CardFace cardFace;
    private final Player player;

    public OthersStartingCardMessage(CardFace cardFace, Player player){
        this.cardFace = cardFace;
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
