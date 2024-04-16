package Server;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;


public class Main {
    private static Controller controller;
    private static ServerConnectionHandler connectionHandler;

    public static void main(String[] args) {
        controller = new Controller();
        try {
            connectionHandler = new ServerConnectionHandler(controller);
            connectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again");
        }

        System.out.println("Starting Controller");
        controller.start();


    }

}
