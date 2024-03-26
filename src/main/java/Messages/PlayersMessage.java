package main.java.Messages;

import main.java.Player.Player;

import java.util.List;
public class PlayersMessage implements GeneralMessage {
    private final List<Player> players;

    PlayersMessage(List<Player> players){
        this.players = players;
    }

    public void printData(){
        System.out.println("Players: ");
        for (Player player : players) {
            System.out.println(player.getName());
        }
    }
}
