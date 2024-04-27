package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;

public class NextTurnMessage implements Serializable, GeneralMessage {
    private final Player player;

    private final int turn;

    public NextTurnMessage(Player player, int turn){
        this.player = player;
        this.turn = turn;
    }


    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute() {

    }
}
