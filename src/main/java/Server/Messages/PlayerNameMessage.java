package Server.Messages;

import Server.Controller.Controller;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private final String name;

    public PlayerNameMessage(String name) {
        this.name = name;
    }

    @Override
    public void serverExecute(Controller controller) {
        controller.addPlayer(this.name);
    }

    @Override
    public void clientExecute() {

    }
}