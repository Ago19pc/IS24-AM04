package Server.Connections;

import Server.Controller.Controller;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used to handle all clients connection of a certain type.
 */
public interface ServerConnectionHandler extends Remote {

    /**
     * Send a message to all the clients
     * @param message the message to send
     * @throws RemoteException when failing to send the message
     */
    void sendAllMessage(ToClientMessage message) throws RemoteException;

    /**
     * Send a message to a specific client
     * @param id the name of the client to send the message to
     * @param message the message to send
     * @throws RemoteException when failing to send the message
     */
    void sendMessage(ToClientMessage message, String id) throws RemoteException;

    /**
     * Execute a message
     * @param message the message to execute
     * @throws RemoteException when failing to execute the message
     */
    void executeMessage(ToServerMessage message) throws RemoteException;

    /**
     * Kill a client
     * @param id the name id the client to kill
     * @throws RemoteException when failing to kill the client
     */
    void killClient(String id) throws RemoteException;

    /**
     * Checks if a client is connected with this connection type
     * @param id the name id the client to check
     * @return true if the client is connected with this connection type, false otherwise
     * @throws RemoteException when failing to check if the client is connected
     */
    boolean isClientAvailable(String id) throws RemoteException;



    /**
     * Set the controller
     * @param controller the controller
     * @throws RemoteException like all RMI stuff
     */

    void setController(Controller controller) throws RemoteException;

    /**
     * This is used by RMI clients to connect to the server
     * @param rmi_port the port of the RMI server
     * @return the message with the required client data
     * @throws RemoteException when failing to connect
     * @throws NotBoundException when the server is not bound
     */
    LobbyPlayersMessage join(int rmi_port) throws RemoteException, NotBoundException;

    /**
     * Responds to a ping from a client
     * @return true if everything is fine
     * @throws RemoteException when failing to respond to the ping
     */
    boolean ping() throws RemoteException;
}
