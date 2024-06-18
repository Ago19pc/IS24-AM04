package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Exception.TooManyPlayersException;

import java.io.Serializable;
import java.rmi.RemoteException;

public class PlayerNameMessage implements ToClientMessage, ToServerMessage, Serializable {
    private String name;
    private final boolean confirmation;
    private String id;

    public PlayerNameMessage(String name, Boolean confirmation) {
        this.name = name;
        this.confirmation = confirmation;
    }

    public PlayerNameMessage(String name, Boolean confirmation, String id) {
        this.name = name;
        this.confirmation = confirmation;
        this.id = id;
    }

    public PlayerNameMessage(Boolean confirmation){this.confirmation = confirmation;}

    @Override
    public void serverExecute(Controller controller){

        try {
            controller.getConnectionHandler().setName(this.name, this.id);
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

    @Override
    public void clientExecute(ClientController controller) {
        controller.setName(confirmation);
    }
}