package Server;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;


public class Main {
    private static Controller controller = new Controller();
    private static ServerConnectionHandler connectionHandler;

    public static void main(String[] args) {
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
