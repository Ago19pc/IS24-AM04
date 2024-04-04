package main;

import Server.Connections.ConnectionHandler;
import Server.Listener.ColorListener;
import Server.EventManager.EventManager;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import Server.Enums.Color;
import Server.Enums.EventType;

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
