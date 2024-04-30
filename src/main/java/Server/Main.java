package Server;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;
import Server.Exception.AlreadySetException;
import Server.Exception.TooFewElementsException;


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
            System.out.println("Ops. Qualcosa è andato storto.");
        }

        System.out.println("Avvio controller");
        /*
        try {
            controller.start();
        } catch (TooFewElementsException | AlreadySetException e) {
            throw new RuntimeException(e);
        }
        */

    }

}
