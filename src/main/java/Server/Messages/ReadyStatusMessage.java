package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.AlreadyStartedException;
import Server.Exception.MissingInfoException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Player.Player;

import java.io.Serializable;

/**
 * Message to ask the server to set a player as ready or not ready and to notify the clients that a player is ready or not ready
 */
public class ReadyStatusMessage implements Serializable, ToClientMessage, ToServerMessage {
    /**
     * True if the player asks to be ready, false otherwise
     */
    private final boolean ready;
    /**
     * The name or id of the player. toClient uses the name, toServer uses the id
     */
    private final String nameOrId;
    /**
     * Constructor
     * @param isReady true if the player is ready, false if the player is not ready
     * @param nameOrId the name or id of the player. toClient uses the name, toServer uses the id
     */
    public ReadyStatusMessage(boolean isReady, String nameOrId) {
        this.ready = isReady;
        this.nameOrId = nameOrId;
    }

    /**
     * Asks the server controller to set a player as ready or not ready
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.nameOrId);
            Player player = controller.getPlayerByName(playerName);
            if(this.ready) {
                controller.setReady(player);
            } else {
                controller.setNotReady(player);
            }
        } catch(PlayerNotFoundByNameException e){
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(nameOrId).sendMessage(message, nameOrId);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (java.rmi.RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (MissingInfoException e){
            ColorNotYetSetMessage colorNotYetSetMessage = new ColorNotYetSetMessage();
            controller.getConnectionHandler().sendMessage(colorNotYetSetMessage, playerName);
        } catch (AlreadyStartedException e ){
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        }

    }

    /**
     * Notifies the clients that a player is ready or not ready
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        controller.updatePlayerReady(this.ready, this.nameOrId);
    }
}
