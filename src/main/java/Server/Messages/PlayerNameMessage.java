package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private String name;
    private boolean confirmation;
    public PlayerNameMessage(String name) {
        this.name = name;
    }
    public PlayerNameMessage(Boolean confirmation){this.confirmation = confirmation;}

    @Override
    public void serverExecute(Controller controller) {
        controller.addPlayer(this.name);
    }

    @Override
    public void clientExecute(ClientController controller) {

       if(confirmation)
           System.out.println("Accepted name");
       else
           System.out.println("Invalid name, please choose another one");

    }
}