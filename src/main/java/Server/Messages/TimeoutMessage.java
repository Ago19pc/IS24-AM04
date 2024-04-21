package Server.Messages;

import Server.Player.Player;

import java.io.Serializable;

public class TimeoutMessage implements Serializable, GeneralMessage{
        private final Player player;

        public TimeoutMessage(Player player){
            this.player = player;
        }
        @Override
        public void printData() {
            System.out.println("Timeout for player " + player.getName() + "!");
        }

        public Player getPlayer(){
            return player;
        }

        public boolean equals(GeneralMessage other){
            System.out.println("TimeoutMessage equals still to be implemented.");
            return this.player.equals(((TimeoutMessage) other).getPlayer());
        }
}
