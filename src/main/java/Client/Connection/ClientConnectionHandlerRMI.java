package Client.Connection;

import Client.Controller.ClientController;
import Server.Connections.ServerConnectionHandler;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class handles the connection between the client and the server using RMI.
 */
public class ClientConnectionHandlerRMI implements ClientConnectionHandler {
    /**
     * The server registry
     */
    Registry serverRegistry;
    /**
     * The server connection handler
     */
    ServerConnectionHandler server;
    /**
     * The client registry
     */
    Registry registry;
    /**
     * The client connection handler to be exported
     */
    ClientConnectionHandler stub;
    /**
     * The client controller
     */
    private ClientController controller;
    /**
     * The client port used for the RMI connection
     */
    int rmi_client_port;

    /**
     * Sets the port for the RMI connection
     * @param rmi_port the port
     */
    public ClientConnectionHandlerRMI(int rmi_port){
        this.rmi_client_port = rmi_port;
    }

    /**
     * Sets the server to connect to using host name
     *
     * @param server_rmi_host the host to connect to
     * @param serverPort the port to connect to
     * @throws RemoteException if the server can't be found
     * @throws NotBoundException if the server is not bound (e.g. trying to connect to a server's socket port)
     */
    public void setServer(String server_rmi_host, int serverPort) throws RemoteException, NotBoundException {
        serverRegistry = LocateRegistry.getRegistry(server_rmi_host, serverPort);
        server = (ServerConnectionHandler) serverRegistry.lookup("ServerConnectionHandler");
    }

    /**
     * Sets the controller. It is used to execute incoming messages
     *
     * @param controller the controller to use
     */
    public void setController(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void sendMessage(ToServerMessage message) {
        if (server == null) {
            System.err.println("Server connection is not initialized!");
            return;
        }
        try {
            server.executeMessage(message);
        } catch (RemoteException e) {
            System.err.println("Error while sending message to the server" + e.getMessage());
        }
    }
    @Override
    public void executeMessage(ToClientMessage message)  {
        message.clientExecute(controller);
    }

    /**
     * Sets the server to connect to using a ServerConnectionHandler
     *
     * @param server the server to connect to
     */
    public void setServer(ServerConnectionHandler server) {
        this.server = server;
    }

    /**
     * @return true because this is an RMI connection
     * @see ClientConnectionHandler
     */
    @Override
    public boolean ping() {
        return true;
    }

    /**
     * Gets the client port used for the RMI connection
     * @return the client port
     */
    public int getRmi_client_port() {
        return rmi_client_port;
    }

    /**
     * Starts the RMI service on the given port. If the port is already in use, it will try the next one.
     * @param rmi_client_port the client port
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
