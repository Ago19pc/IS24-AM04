package Server.Messages;

import Server.Controller.Controller;
import Server.Player.Player;

import java.io.Serializable;

public class TimeoutMessage implements Serializable, GeneralMessage{
        private final Player player;

        public TimeoutMessage(Player player){
            this.player = player;
        }

    @Override
    public void serverExecute(Controller controller) {

    }

    @Override
    public void clientExecute() {

    }
}
