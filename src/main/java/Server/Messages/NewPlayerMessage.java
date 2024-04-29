package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;

public class NewPlayerMessage implements Serializable, GeneralMessage {

    private  final List<Player> p;


    public NewPlayerMessage(List<Player> p) {
        this.p = p;
    }

    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }
    @Override
    public void clientExecute(ClientController controller) {
        controller.setPlayerList(p);
    }
}
