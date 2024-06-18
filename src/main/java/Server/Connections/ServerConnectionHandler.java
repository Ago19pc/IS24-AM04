package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.TooManyPlayersException;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface is used to handle all clients connection of a certain type.
 */
public interface ServerConnectionHandler extends Remote {

    /**
     * Send a message to all the clients
     * @param message the message to send
     */
    void sendAllMessage(ToClientMessage message) throws RemoteException;

    /**
     * Send a message to a specific client
     * @param id the name of the client to send the message to
     * @param message the message to send
     */
    void sendMessage(ToClientMessage message, String id) throws RemoteException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    void executeMessage(ToServerMessage message) throws RemoteException;

    /**
     * Kill a client
     * @param id the name id the client to kill
     */
    void killClient(String id) throws RemoteException;

    /**
     * Checks if a client is connected with this connection type
     * @param id the name id the client to check
     */
    boolean isClientAvailable(String id) throws RemoteException;

    /**
     * Get all the ids of the clients connected with this connection type
     * @return a list of all the ids of the clients connected
     */
    List<String> getAllIds() throws RemoteException;



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
     */
    LobbyPlayersMessage join(int rmi_port) throws RemoteException, NotBoundException;
}
