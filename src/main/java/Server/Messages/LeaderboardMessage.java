package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Message to send the client the leaderboard
 */
public class LeaderboardMessage implements Serializable, ToClientMessage {
    /**
     * The leaderboard to send. It is a map with the player names as keys and their points as values, sorted by order of winning
     */
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
