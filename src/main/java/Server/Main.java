package Server;

import Server.Connections.GeneralServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;


public class Main {


    public static void main() {
        try {
            GeneralServerConnectionHandler connectionHandler = new GeneralServerConnectionHandler();
            Controller controller = new ControllerInstance(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.start();
        } catch (Exception e) {
            System.err.println("Ops. Qualcosa è andato storto.");
        }

        System.out.println("Avvio controller");


    }

}
