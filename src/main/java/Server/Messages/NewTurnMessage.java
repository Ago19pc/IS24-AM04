package Server.Messages;

import java.io.Serializable;

/**
 * Message to inform the client that a new turn has started
 */
public class NewTurnMessage implements Serializable, ToClientMessage{
    private String activePlayerName;
    private int turnNumber;

    public NewTurnMessage(String activePlayerName, int turnNumber) {
        this.activePlayerName = activePlayerName;
        this.turnNumber = turnNumber;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller){
        controller.newTurn(activePlayerName, turnNumber);
    }
}
