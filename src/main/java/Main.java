package main.java;

import main.java.Connections.ConnectionHandler;
import main.java.Listener.ColorListener;
import main.java.EventManager.EventManager;
import main.java.GameModel.GameModel;
import main.java.GameModel.GameModelInstance;
import main.java.Player.Player;
import main.java.Player.PlayerInstance;
import main.java.Enums.Color;
import main.java.Enums.EventType;

public class Main {
    public static void main(String[] args) {
        /*EventManager eventManager = new EventManager( );
        ColorListener listener1 = new ColorListener();
        ColorListener listener2 = new ColorListener();
        eventManager.subscribe(EventType.SET_COLOR, listener1);
        eventManager.subscribe(EventType.SET_COLOR, listener2);
        Player player = new PlayerInstance("Ago19", eventManager);
        player.setColor(Color.RED);

        GameModel gameModel = new GameModelInstance();
        */
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler(1234);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
