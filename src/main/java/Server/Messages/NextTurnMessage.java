package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;

public class NextTurnMessage implements Serializable, GeneralMessage {

    private final int turn;

    private final String activePlayerName;

    public NextTurnMessage(int turn, String activePlayerName){
        this.turn = turn;
        this.activePlayerName = activePlayerName;
    }


    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute(ClientController controller) {

        controller.setActivePlayerName(this.activePlayerName);
        controller.setTurn(this.turn);

    }
}
