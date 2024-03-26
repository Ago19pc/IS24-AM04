package main.java.Server.Messages;

import main.java.Server.Player.Player;

import java.util.Map;

public class LeaderboardMessage implements GeneralMessage {

        private final Map<Player, Integer> points;

        public LeaderboardMessage(Map<Player, Integer> points){
            this.points = points;
        }
        @Override
        public void printData() {
            System.out.println("Leaderboard: ");
            for (Player player : points.keySet()) {
                System.out.println(player.getName() + ": " + points.get(player) + " points");
            }
        }
}
