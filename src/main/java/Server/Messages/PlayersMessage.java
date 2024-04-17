package Server.Messages;

import Server.Player.Player;

import java.util.List;
public class PlayersMessage implements GeneralMessage {
    private final List<Player> players;

    public PlayersMessage(List<Player> players){
        this.players = players;
    }

    public void printData(){
        System.out.println("Players: ");
        for (Player player : players) {
            System.out.println(player.getName());
        }
    }
    public List<Player> getPlayers(){
        return players;
    }

    public boolean equals(GeneralMessage other){
        System.out.println("PlayersMessage equals still to be implemented.");
        return this.players.equals(((PlayersMessage) other).getPlayers());
    }
}
