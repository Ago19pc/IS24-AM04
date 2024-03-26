package main.java.Messages;

import main.java.Player.Player;
public class NextTurnMessage implements GeneralMessage {
    private final Player player;

    private final int turn;

    public NextTurnMessage(Player player, int turn){
        this.player = player;
        this.turn = turn;
    }

    public void printData(){
        System.out.println("Player " + player.getName() + " is next in turn " + turn);
    }
}
