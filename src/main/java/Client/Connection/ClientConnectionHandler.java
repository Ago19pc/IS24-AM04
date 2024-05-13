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
    public void sendMessage(ToServerMessage message) throws IOException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    public void executeMessage(ToClientMessage message) throws RemoteException;


}
