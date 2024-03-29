package main.java;

import main.java.Server.Connections.ConnectionHandler;
import main.java.Server.Listener.ColorListener;
import main.java.Server.EventManager.EventManager;
import main.java.Server.GameModel.GameModel;
import main.java.Server.GameModel.GameModelInstance;
import main.java.Server.Player.Player;
import main.java.Server.Player.PlayerInstance;
import main.java.Server.Enums.Color;
import main.java.Server.Enums.EventType;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inputReader = new Scanner(System.in);

        EventManager eventManager = new EventManager( );
        ColorListener listener1 = new ColorListener();
        ColorListener listener2 = new ColorListener();
        eventManager.subscribe(EventType.SET_COLOR, listener1);
        eventManager.subscribe(EventType.SET_COLOR, listener2);
        Player player = new PlayerInstance("Ago19", eventManager);
        player.setColor(Color.RED);

        GameModel gameModel = new GameModelInstance();

        System.out.println("Hello my man, what port would you like to start your server on?");
        int port = inputReader.nextInt();

        try {
            ConnectionHandler connectionHandler = new ConnectionHandler(port);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
