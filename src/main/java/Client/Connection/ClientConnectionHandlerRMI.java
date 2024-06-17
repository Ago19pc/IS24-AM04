package Client.Connection;

import Client.Controller.ClientController;
import Server.Connections.ServerConnectionHandler;
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

    /**
     * Sets the port for the RMI connection
     * @param rmi_port, the port
     * @throws RemoteException like all RMI stuff
     */
    public ClientConnectionHandlerRMI(int rmi_port) throws RemoteException {
        this.rmi_client_port = rmi_port;
    }

    public void setServer(String server_rmi_host, int serverPort) throws RemoteException {
        serverRegistry = LocateRegistry.getRegistry(server_rmi_host, serverPort);
        try {
            server = (ServerConnectionHandler) serverRegistry.lookup("ServerConnectionHandler");
        } catch (Exception e) {
            System.out.println("[RMI] Error with registryLookup");
        }

    }

    /**
     * Sets the controller
     * @param controller the ClientController
     */
    public void setController(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Send a message to the server
     *
     * @param message the message to send
     */

    @Override
    public void sendMessage(ToServerMessage message) {
        if (server == null) {
            System.err.println("Server connection is not initialized!");
            return;
        }
        try {
            server.executeMessage(message);
        } catch (RemoteException e) {
            System.err.println("Error while sending message to the server");
        }
    }
    /**
     * Execute a message
     *
     * @param message the message to execute
     */
    @Override
    public void executeMessage(ToClientMessage message)  {
        message.clientExecute(controller);
    }

    /**
     * Save the server in the local variable
     */
    public void setServer(ServerConnectionHandler server) {
        this.server = server;
    }

    /**
     * For the ping returns true
     */
    public boolean ping() {
        return true;
    }

    /**
     * Get the RMI client port
     * @return the RMI client port
     */
    public int getRmi_client_port() {
        return rmi_client_port;
    }

    /**
     * Set the RMI client port
     * @param rmi_client_port the port of RMI client
     */
    public void setRmi_client_port(int rmi_client_port) {
        this.rmi_client_port = rmi_client_port;
        try {
            stub = (ClientConnectionHandler) UnicastRemoteObject.exportObject(this, rmi_client_port);
            registry = LocateRegistry.createRegistry(rmi_client_port);
            registry.rebind("ClientConnectionHandler", stub);
            System.out.println("[RMI] Service started on port: " + rmi_client_port);
        } catch (Exception e) {
            System.out.println("[RMI] Error with selected port trying with next one...");
            setRmi_client_port(rmi_client_port + 1);
        }
    }
}
