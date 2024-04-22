package Server.Messages;

import Server.Controller.Controller;
import Server.Player.Player;

import java.io.Serializable;
import java.util.Map;

public class LeaderboardMessage implements Serializable, GeneralMessage {

        private final Map<Player, Integer> points;

        public LeaderboardMessage(Map<Player, Integer> points){
            this.points = points;
        }


    @Override
    public void serverExecute(Controller controller) {

    }

    @Override
    public void clientExecute() {

    }
}
