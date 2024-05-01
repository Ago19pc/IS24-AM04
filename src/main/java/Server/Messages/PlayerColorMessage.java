package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class PlayerColorMessage implements Serializable, GeneralMessage {

    private String name;
    private Color color;
    private boolean confirmation;
    private boolean own;


    public PlayerColorMessage(Boolean confirmation, String name, Color color, boolean own){
        this.confirmation = confirmation;
        this.name = name;
        this.color = color;
        this.own = own;
    }

    @Override
    public void serverExecute(Controller controller) {
       try{
           controller.setPlayerColor(this.color, controller.getPlayerByName(this.name));
       }catch (PlayerNotFoundByNameException | IllegalArgumentException e){
           PlayerColorMessage playerColorMessage = new PlayerColorMessage(false, this.name, this.color, this.own);
           controller.getConnectionHandler().sendMessage(playerColorMessage, this.name);
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
