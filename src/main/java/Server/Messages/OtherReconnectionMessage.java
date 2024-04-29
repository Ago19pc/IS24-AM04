package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;

public class OtherReconnectionMessage implements Serializable, GeneralMessage {

    private Player player;
    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute(ClientController controller) {
        for(Player p : controller.getPlayers()) {
            if (p.getName() == player.getName())
                p = player;
        }
    }
}
