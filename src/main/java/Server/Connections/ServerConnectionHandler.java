package Server.Connections;

import Server.Controller.Controller;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerConnectionHandler extends Remote {

    /**
     * Send a message to all the clients
     * @param message the message to send
     */
    public void sendAllMessage(ToClientMessage message) throws RemoteException;

    /**
     * Send a message to a specific client
     * @param name the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(ToClientMessage message, String name) throws RemoteException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    public void executeMessage(ToServerMessage message) throws RemoteException;

    /**
     * Kill a client
     * @param name the name of the client to kill
     */
    public void killClient(String name) throws RemoteException;

    /**
     * Set the name of a client
     * @param host
     * @param port
     * @param name
     * @throws RemoteException
     */
    public void setName(String host, int port, String name) throws RemoteException;

    /**
     * Check if a client name is available
     * @param name
     * @return
     * @throws RemoteException
     */

    public boolean isClientNameAvailable(String name) throws RemoteException;

    /**
     * Check if a client address is available
     * @param host
     * @param port
     * @return
     * @throws RemoteException
     */

    public boolean isClientAddressAvailable(String host, int port) throws RemoteException;

    /**
     * Set the controller
     * @param controller
     * @throws RemoteException
     */

    public void setController(Controller controller) throws RemoteException;

}
