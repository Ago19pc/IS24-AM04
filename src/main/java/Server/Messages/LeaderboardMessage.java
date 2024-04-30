package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LeaderboardMessage implements Serializable, GeneralMessage {

        private final List<Player> p;

        public LeaderboardMessage(List<Player> list ){
            this.p = list ;
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
