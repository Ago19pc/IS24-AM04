package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LobbyPlayersMessage implements Serializable, ToClientMessage {
    private List<String> playerNames;
    private Map<String, Color> playerColors;
    private Map<String, Boolean> playerReady;

    private String id;

    public LobbyPlayersMessage(List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, String id) {
        this.playerNames = playerNames;
        this.playerColors = playerColors;
        this.playerReady = playerReady;
        this.id = id;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException, PlayerNotFoundByNameException {
        controller.loadLobbyInfo(id, playerNames, playerColors, playerReady);
    }
}
