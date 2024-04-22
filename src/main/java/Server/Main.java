package Server;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;


public class Main {
    private static ServerConnectionHandler connectionHandler;
    private static Controller controller;


    public static void main(String[] args) {
        try {
            connectionHandler = new ServerConnectionHandler();
            controller = new ControllerInstance(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again");
        }

        System.out.println("Starting Controller");
        controller.start();


    }

}
