package Server.Messages;

import Client.Controller.ClientController;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;

public class NewPlayerMessage implements Serializable, ToClientMessage {

    private final List<String> playerNames;

    public NewPlayerMessage(List<Player> p) {
        this.playerNames = p.stream().map(Player::getName).toList();
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.newPlayer(playerNames);
    }
}
