package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class LeaderboardMessage implements Serializable, ToClientMessage {

    private final LinkedHashMap<String, Integer> playerPoints;

    public LeaderboardMessage(LinkedHashMap<String, Integer> playerPoints){
        this.playerPoints = playerPoints;
    }


    @Override
    public void clientExecute(ClientController controller) {
            controller.displayLeaderboard(playerPoints);
    }
}
