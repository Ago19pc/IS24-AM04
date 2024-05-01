package Client.Connection;

import Client.Controller.ClientController;
import Server.Connections.ServerConnectionHandler;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientConnectionHandlerRMI implements ClientConnectionHandler {
    Registry serverRegistry;
    ServerConnectionHandler server;

    Registry registry;
    ClientConnectionHandler stub;

    private ClientController controller;


    public ClientConnectionHandlerRMI(String server_rmi_host) throws RemoteException, NotBoundException {
        System.out.println("RMI CONNECTING");
        serverRegistry = LocateRegistry.getRegistry(server_rmi_host, 1099);
        System.out.println("GETTED REGISTRY");
        try {
            server = (ServerConnectionHandler) serverRegistry.lookup("ServerConnectionHandler");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("GETTED SERVER");

        stub = (ClientConnectionHandler) UnicastRemoteObject.exportObject((ClientConnectionHandler) this, 1099);
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind("ClientConnectionHandler", stub);

    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Send a message to the server
     *
     * @param message the message to send
     * @throws IOException
     */
    @Override
    public void sendMessage(GeneralMessage message) throws IOException {
        server.executeMessage(message);
    }

    /**
     * Execute a message
     *
     * @param message the message to execute
     */
    @Override
    public void executeMessage(GeneralMessage message)  {
        try {
            message.clientExecute(controller);
        } catch (ClientExecuteNotCallableException e) {
            System.out.println("This message should not be executed by client");
        }
    }


}
