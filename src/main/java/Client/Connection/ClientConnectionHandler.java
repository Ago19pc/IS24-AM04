package Client.Connection;

import Server.Messages.ToServerMessage;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used to send messages to the server and execute messages received from the server.
 */
public interface ClientConnectionHandler extends Remote {


    /**
     * Sends a message to the server
     * @param message the message to send
     */
    public void sendMessage(ToServerMessage message) throws IOException;

    /**
     * Executes a message
     * This method is called by ServerConnectionHandlerRMI to execute a message on the client side
     * @param message the message to execute
     */
    public void executeMessage(ToClientMessage message) throws RemoteException;

    /**
     * Responds to an RMI ping from the server.
     * This method is called by ServerConnectionHandlerRMI to check if its clients are still connected.
     * @return true if the RMI client is still connected. If it's not connected, a RemoteException is thrown. False is returned if the client is not an RMI client
     */
    public boolean ping() throws RemoteException;
}
