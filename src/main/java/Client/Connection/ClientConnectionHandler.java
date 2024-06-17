package Client.Connection;

import Server.Messages.ToServerMessage;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionHandler extends Remote {
    /**
     * Send a message to the server
     * @param message the message to send
     * @throws IOException
     */
    void sendMessage(ToServerMessage message) throws IOException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    void executeMessage(ToClientMessage message) throws RemoteException;

    /**
     * Ping the server
     * @throws RemoteException
     */
    boolean ping() throws RemoteException;
}
