package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Color;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Message to send the client the info of the players in the lobby. Used both for the new game lobby and the saved game lobby
 */
public class LobbyPlayersMessage implements Serializable, ToClientMessage {
    /**
     * A list of the names of the players in the lobby
     */
    private final List<String> playerNames;
    /**
     * A map of the colors of the players in the lobby
     */
    private final Map<String, Color> playerColors;
    /**
     * A map of the ready status of the players in the lobby
     */
    private final Map<String, Boolean> playerReady;
    /**
     * The client's id
     */
    private final String id;
    /**
     * Whether the game is a saved game or not
     */
    private final Boolean isSavedGame;

    /**
     * Constructor
     * @param playerNames the names of the players in the lobby
     * @param playerColors the colors of the players in the lobby
     * @param playerReady the ready status of the players in the lobby
     * @param id the id of the game
     * @param isSavedGame whether the game is a saved game or not
     */
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
