package Server.Messages;

import java.io.Serializable;

/**
 * Message to inform the client that a new turn has started
 */
public class NewTurnMessage implements Serializable, ToClientMessage{
    /**
     * The name of the active player
     */
    private final String activePlayerName;
    /**
     * The current turn number
     */
    private final int turnNumber;
    /**
     * Constructor
     * @param activePlayerName the name of the active player
     * @param turnNumber the number of the turn
     */
    public NewTurnMessage(String activePlayerName, int turnNumber) {
        this.activePlayerName = activePlayerName;
        this.turnNumber = turnNumber;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller){
        controller.newTurn(activePlayerName, turnNumber);
    }
}
