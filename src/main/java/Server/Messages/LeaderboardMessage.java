package Server.Messages;

import Server.Player.Player;

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

        public Map<Player, Integer> getPoints(){
            return points;
        }

        public boolean equals(GeneralMessage other){
            System.out.println("LeaderboardMessage equals still to be implemented.");
            return this.points.equals(((LeaderboardMessage) other).getPoints());
        }
}
