package Server.Messages;

import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;
import java.util.Objects;

public class PlayerColorMessage implements Serializable, ToClientMessage, ToServerMessage {

    private String name;
    private Color color;
    private boolean confirmation;
    private String id;

    public PlayerColorMessage(Color color, String id){
        this.color = color;
        this.id = id;
    }

    public PlayerColorMessage(Boolean confirmation, String name, Color color){
        this.confirmation = confirmation;
        this.name = name;
        this.color = color;
    }

    @Override
    public void serverExecute(Controller controller) throws PlayerNotFoundByNameException {
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
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to client based on ip
        }
    }

    @Override
    public void clientExecute(ClientController controller) {
        if(!Objects.equals(this.name, controller.getMyName())){
            controller.updatePlayerColors(this.color, this.name);
        } else {
            controller.setColor(confirmation, this.color);
        }
    }
}
