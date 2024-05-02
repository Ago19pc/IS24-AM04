package Client.Connection;

import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionHandler extends Remote {
    /**
     * Send a message to the server
     * @param message the message to send
     * @throws IOException
     */
    public void sendMessage(GeneralMessage message) throws IOException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    public void executeMessage(GeneralMessage message) throws RemoteException;


}
