package Server.Messages;

import Server.Player.Player;
public class TimeoutMessage implements GeneralMessage {
        private final Player player;

        public TimeoutMessage(Player player){
            this.player = player;
        }
        @Override
        public void printData() {
            System.out.println("Timeout for player " + player.getName() + "!");
        }
}
