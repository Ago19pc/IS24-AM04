package Server;

import Server.Connections.GeneralServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;

/**
 * Main class of the server
 */
public class Main {
    private static GeneralServerConnectionHandler connectionHandler;
    private static Controller controller;

    /**
     * Main method of the server
     * Creates the connection handler and the controller, then starts the socket connection handler
     */
    public static void main() {
        try {
            connectionHandler = new GeneralServerConnectionHandler();
            controller = new ControllerInstance(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ops. Qualcosa è andato storto.");
        }

        System.out.println("Avvio controller");

    }

}
