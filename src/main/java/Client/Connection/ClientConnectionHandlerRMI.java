package Client.Connection;

import Client.Controller.ClientController;
import Server.Connections.ServerConnectionHandler;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class handles the connection between the client and the server using RMI
 * The ClientConnectionHandlerRMI port is set to 1100
 */
public class ClientConnectionHandlerRMI implements ClientConnectionHandler {
    Registry serverRegistry;
    ServerConnectionHandler server;

    Registry registry;
    ClientConnectionHandler stub;

    private ClientController controller;
    int rmi_client_port;

    public ClientConnectionHandlerRMI() throws RemoteException {

    }

    public void setServer(String server_rmi_host) throws RemoteException {
        serverRegistry = LocateRegistry.getRegistry(server_rmi_host, 1099);
        try {
            server = (ServerConnectionHandler) serverRegistry.lookup("ServerConnectionHandler");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void sendMessage(ToServerMessage message) throws IOException {
        server.executeMessage(message);
    }

    /**
     * Execute a message
     *
     * @param message the message to execute
     */
    @Override
    public void executeMessage(ToClientMessage message)  {
        try {
            message.clientExecute(controller);
        } catch (ClientExecuteNotCallableException e) {
            System.out.println("This message should not be executed by client");
        } catch (PlayerNotFoundByNameException e) {
            System.out.println("Player not found by name");
        }
    }

    public boolean ping() {
        return true;
    }

    public int getRmi_client_port() {
        return rmi_client_port;
    }

    public void setRmi_client_port(int rmi_client_port) {
        this.rmi_client_port = rmi_client_port;
        try {
            stub = (ClientConnectionHandler) UnicastRemoteObject.exportObject((ClientConnectionHandler) this, rmi_client_port);
            registry = LocateRegistry.createRegistry(rmi_client_port);
            registry.rebind("ClientConnectionHandler", stub);
            System.out.println("[RMI] Service started on port: " + rmi_client_port);
        } catch (Exception e) {
            System.out.println("[RMI] Error with selected port trying with next one...");
            setRmi_client_port(rmi_client_port + 1);

        }
    }
}
