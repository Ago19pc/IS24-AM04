package run;

import Client.Controller.ClientController;
import Client.Controller.ClientControllerInstance;
import Client.View.CLI;

import java.rmi.RemoteException;

public class MainCLI {
    public static void main() throws RemoteException {
        ClientController controller = new ClientControllerInstance();
        CLI cli = new CLI(controller);
        cli.start();
        controller.main(cli);
    }
}
