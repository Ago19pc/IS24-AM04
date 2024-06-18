package Interface;

import Client.Controller.ClientController;
import Client.Controller.ClientControllerInstance;
import Client.View.CLI;

import java.rmi.RemoteException;

/**
 * This class is responsible for starting the CLI.
 */
public class MainCLI {
    /**
     * Main method that starts the CLI
     */
    public static void main() throws RemoteException {
        ClientController controller = new ClientControllerInstance();
        CLI cli = new CLI(controller);
        cli.start();
        controller.main(cli);
    }
}
