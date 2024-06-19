package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.Objects;

/**
 * Message to ask the server to set the color and to notify the client that a player has set his color
 */
public class PlayerColorMessage implements Serializable, ToClientMessage, ToServerMessage {
    /**
     * The name of the player
     */
    private String name;
    /**
     * The color the player sets
     */
    private final Color color;
    /**
     * True if the color was set, false otherwise
     */
    private boolean confirmation;
    /**
     * The id of the client
     */
    private String id;

    /**
     * ToServer constructor
     * @param color the color the player wants to set
     * @param id  the id of the client
     */
    public PlayerColorMessage(Color color, String id){
        this.color = color;
        this.id = id;
    }

    /**
     * ToClient constructor
     * @param confirmation true if the color was set, false otherwise
     * @param name the name of the player
     * @param color the color the player set
     */
    public PlayerColorMessage(Boolean confirmation, String name, Color color){
        this.confirmation = confirmation;
        this.name = name;
        this.color = color;
    }

    /**
     * Asks the server controller to set the color of the player
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller){
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.id);
            Player player = controller.getPlayerByName(playerName);
            controller.setPlayerColor(this.color, player);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        } catch (IllegalArgumentException e) {
            PlayerColorMessage playerColorMessage = new PlayerColorMessage(false, playerName, this.color);
            controller.getConnectionHandler().sendMessage(playerColorMessage, playerName);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (java.rmi.RemoteException exception) {
                System.out.println("Remote exception");
            }
        }
    }

    /**
     * Notifies the client controller that a player has set his color
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        if(!Objects.equals(this.name, controller.getMyName())){
            controller.updatePlayerColors(this.color, this.name);
        } else {
            controller.setColor(confirmation, this.color);
        }
    }
}
