package Server.Messages;

import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class PlayerColorMessage implements Serializable, ToClientMessage, ToServerMessage {

    private String name;
    private Color color;
    private boolean confirmation;
    private boolean own;

    public PlayerColorMessage(Color color){
        this.color = color;
    }

    public PlayerColorMessage(Boolean confirmation, String name, Color color, boolean own){
        this.confirmation = confirmation;
        this.name = name;
        this.color = color;
        this.own = own;
    }

    @Override
    public void serverExecute(Controller controller) throws PlayerNotFoundByNameException {
        String playerName = "";
        try {
            ClientHandler client = controller.getConnectionHandler().getThreads()
                    .stream().filter(c -> c.getReceiver().threadId() == Thread.currentThread().threadId()).toList().getFirst();
            playerName = controller.getConnectionHandler().getThreadName(client);
            Player player = controller.getPlayerByName(playerName);
            controller.setPlayerColor(this.color, player);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        } catch (IllegalArgumentException e) {
            PlayerColorMessage playerColorMessage = new PlayerColorMessage(false, playerName, this.color, true);
            controller.getConnectionHandler().sendMessage(playerColorMessage, playerName);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to client based on ip
        }
    }

    @Override
    public void clientExecute(ClientController controller) {
        if(!this.own){
            controller.updatePlayerColors(this.color, this.name);
        } else {
            controller.setColor(confirmation, this.color);
        }
    }
}
