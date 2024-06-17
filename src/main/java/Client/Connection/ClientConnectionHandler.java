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
     * @throws IOException when the message can't be sent
     */
    void sendMessage(ToServerMessage message) throws IOException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    void executeMessage(ToClientMessage message) throws RemoteException;

    /**
     * Ping the server
     * @throws RemoteException like all RMI stuff
     */
    boolean ping() throws RemoteException;
}
