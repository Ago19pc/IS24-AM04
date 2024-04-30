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


    public PlayerColorMessage(Boolean confirmation){this.confirmation = confirmation;}

    public PlayerColorMessage(String name, Color color)
    {
        this.name = name;
        this.color = color;
    }

    @Override
    public void serverExecute(Controller controller) {
       try{
           controller.setPlayerColor(this.color, controller.getPlayerByName(this.name));

           PlayerColorMessage playerColorMessage = new PlayerColorMessage(true);
           controller.getConnectionHandler().sendMessage(playerColorMessage, this.name);
       }catch (PlayerNotFoundByNameException | IllegalArgumentException e){
           PlayerColorMessage playerColorMessage = new PlayerColorMessage(false);
           controller.getConnectionHandler().sendMessage(playerColorMessage, this.name);
       }

    }

    @Override
    public void clientExecute(ClientController controller) {

        if(confirmation){
            controller.setColor();
            System.out.println("Accepted color");}
        else
            System.out.println("Color not avaiable");

    }
}
