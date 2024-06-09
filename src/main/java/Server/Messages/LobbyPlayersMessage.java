package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Color;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LobbyPlayersMessage implements Serializable, ToClientMessage {
    private List<String> playerNames;
    private Map<String, Color> playerColors;
    private Map<String, Boolean> playerReady;

    private String id;
    private Boolean isSavedGame;

    public LobbyPlayersMessage(List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, String id, Boolean isSavedGame) {
        this.playerNames = playerNames;
        this.playerColors = playerColors;
        this.playerReady = playerReady;
        this.id = id;
        this.isSavedGame = isSavedGame;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.loadLobbyInfo(id, playerNames, playerColors, playerReady, isSavedGame);
    }
}
