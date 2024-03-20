package main.java;

import main.java.Client.ColorListener;
import main.java.Client.EventManager;
import main.java.Player.Player;
import main.java.Player.PlayerInstance;
import main.java.Enums.Color;

public class Main {
    public static void main(String[] args) {
        EventManager eventManager = new EventManager( );
        ColorListener listener = new ColorListener(eventManager);
        Player player = new PlayerInstance("Ago19", eventManager);
        player.setColor(Color.RED);
    }
}
