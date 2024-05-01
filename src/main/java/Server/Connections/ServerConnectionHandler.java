package Server.Connections;

import Server.Controller.Controller;
import Server.Messages.GeneralMessage;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerConnectionHandler extends Remote {

    /**
     * Send a message to all the clients
     * @param message the message to send
     */
    public void sendAllMessage(GeneralMessage message);

    /**
     * Send a message to a specific client
     * @param name the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(GeneralMessage message, String name);

    /**
     * Execute a message
     * @param message the message to execute
     */
    public void executeMessage(GeneralMessage message) throws RemoteException;

    /**
     * Kill a client
     * @param name the name of the client to kill
     */
    public void killClient(String name);

    public void setName(String host, int port, String name);

    public boolean isClientNameAvailable(String name);

    public boolean isClientAddressAvailable(String host, int port);

    public void setController(Controller controller);

}
