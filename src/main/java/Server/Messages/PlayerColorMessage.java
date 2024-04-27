package Server.Messages;

import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class PlayerColorMessage implements Serializable, GeneralMessage {

    private final String name;
    private final Color color;
    public PlayerColorMessage(String name, Color color)
    {
        this.name = name;
        this.color = color;
    }

    @Override
    public void serverExecute(Controller controller) {
       try{
           controller.setPlayerColor(this.color, controller.getPlayerByName(this.name));
       }catch (PlayerNotFoundByNameException e){
           e.printStackTrace();
       }

    }

    @Override
    public void clientExecute() {

    }
}
