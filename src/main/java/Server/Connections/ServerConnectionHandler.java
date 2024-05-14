package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerConnectionHandler extends Remote {

    /**
     * Send a message to all the clients
     * @param message the message to send
     */
    public void sendAllMessage(ToClientMessage message) throws RemoteException;

    /**
     * Send a message to a specific client
     * @param id the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(ToClientMessage message, String id) throws RemoteException;

    /**
     * Execute a message
     * @param message the message to execute
     */
    public void executeMessage(ToServerMessage message) throws RemoteException;

    /**
     * Kill a client
     * @param name the name of the client to kill
     */
    public void killClient(String name) throws RemoteException, PlayerNotFoundByNameException, AlreadyFinishedException;

    public void setName(String name, String clientID) throws RemoteException;

    public boolean isClientAvailable(String id) throws RemoteException;

    public List<String> getAllIds() throws RemoteException;



    /**
     * Set the controller
     * @param controller
     * @throws RemoteException
     */

    public void setController(Controller controller) throws RemoteException;

    public LobbyPlayersMessage join(int rmi_port) throws RemoteException, NotBoundException;
}
