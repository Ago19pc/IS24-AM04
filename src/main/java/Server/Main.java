package Server;

import Server.Client.Client;
import Server.Connections.ConnectionHandler;
import Server.Controller.Controller;


public class Main {
    private static Controller controller;
    private static ConnectionHandler connectionHandler;

    public static void main() {
        controller = new Controller();
        try {
            connectionHandler = new ConnectionHandler(controller);
            connectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again");
        }

        System.out.println("Starting Controller");
        controller.start();


    }

}
