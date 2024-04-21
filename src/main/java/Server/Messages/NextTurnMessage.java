package Server.Messages;

import Server.Player.Player;

import java.io.Serializable;

public class NextTurnMessage implements Serializable, GeneralMessage {
    private final Player player;

    private final int turn;

    public NextTurnMessage(Player player, int turn){
        this.player = player;
        this.turn = turn;
    }

    public Player getPlayer(){
        return player;
    }

    public int getTurn(){
        return turn;
    }
    public void printData(){
        System.out.println("Player " + player.getName() + " is next in turn " + turn);
    }

    public boolean equals(GeneralMessage other){
        System.out.println("NextTurnMessage equals still to be implemented.");
        return this.player.equals(((NextTurnMessage) other).getPlayer()) && this.turn == ((NextTurnMessage) other).getTurn();
    }
}
