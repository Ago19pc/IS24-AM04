package Server;

import Server.Connections.GeneralServerConnectionHandler;
import Server.Connections.ServerConnectionHandlerSOCKET;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;


public class Main {
    private static GeneralServerConnectionHandler connectionHandler;
    private static Controller controller;


    public static void main(String[] args) {
        try {
            connectionHandler = new GeneralServerConnectionHandler();
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
