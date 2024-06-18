package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Message to send the client the leaderboard
 */
public class LeaderboardMessage implements Serializable, ToClientMessage {

    private final LinkedHashMap<String, Integer> playerPoints;

    /**
     * Constructor
     * @param playerPoints the leaderboard to send
     */
    public LeaderboardMessage(LinkedHashMap<String, Integer> playerPoints){
        this.playerPoints = playerPoints;
    }


    @Override
    public void clientExecute(ClientController controller) {
            controller.displayLeaderboard(playerPoints);
    }
}
