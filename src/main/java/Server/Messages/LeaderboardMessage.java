package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.Map;

public class LeaderboardMessage implements Serializable, ToClientMessage {

    private Map<String, Integer> playerPoints;

    public LeaderboardMessage(Map<String, Integer> playerPoints){
        this.playerPoints = playerPoints;
    }


    @Override
    public void clientExecute(ClientController controller) {
            controller.displayLeaderboard(playerPoints);
    }
}
