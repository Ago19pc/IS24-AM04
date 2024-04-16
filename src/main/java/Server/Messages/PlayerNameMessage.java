package Server.Messages;

import Server.Player.Player;

import java.io.Serializable;
import java.util.List;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private final String name;

    public PlayerNameMessage(String name){
        this.name = name;
    }

    public void printData(){
        System.out.println("Player name: " + name);
    }
    public String getName(){
        return name;
    }
}