package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Exception.TooManyPlayersException;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Message to ask the server to add a player with a certain name and to notify the client if the name has been set correctly
 */
public class PlayerNameMessage implements ToClientMessage, ToServerMessage, Serializable {
    /**
     * The name to set
     */
    private final String name;
    /**
     * True if the name was set, false otherwise
     */
    private boolean confirmation;
    /**
     * The id of the client
     */
    private String id;

    /**
     * ToClient constructor
     * @param name the name of the player
     * @param confirmation true if the name was set, false otherwise
     */
    public PlayerNameMessage(String name, Boolean confirmation) {
        this.name = name;
        this.confirmation = confirmation;
    }

    /**
     * ToServer constructor
     * @param name the name of the player
     * @param id the id of the client
     */
    public PlayerNameMessage(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Asks the server controller to add a player with a certain name
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller){

        try {
            controller.addPlayer(this.name, this.id);
        } catch (IllegalArgumentException e) {
            InvalidNameMessage message = new InvalidNameMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (TooManyPlayersException e) {
            TooManyPlayersMessage message = new TooManyPlayersMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage message = new GameAlreadyStartedMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (RemoteException exception) {
                System.out.println("Remote exception");
            }
        }

    }

    /**
     * Notifies the client with the outcome of his request
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        controller.setName(confirmation);
    }
}