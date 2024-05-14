package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class PlayerDisconnectedMessage implements Serializable, ToClientMessage{
    private final String playerName;

    public PlayerDisconnectedMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException, PlayerNotFoundByNameException {
        controller.playerDisconnected(playerName);
    }
}
