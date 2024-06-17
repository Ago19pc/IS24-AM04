package Interface;

import Client.Controller.ClientController;
import Client.View.CLI;
import Client.View.UI;

import java.rmi.RemoteException;

public class MainCLI {
    private static ClientController controller;
    private static CLI cli;
    public static void main() throws RemoteException {
        controller = new ClientController();
        cli = new CLI(controller);
        cli.start();
        controller.main((UI) cli);
    }
}
