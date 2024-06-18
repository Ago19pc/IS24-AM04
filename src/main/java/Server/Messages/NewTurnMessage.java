package Server.Messages;

import java.io.Serializable;

public class NewTurnMessage implements Serializable, ToClientMessage{
    private final String activePlayerName;
    private final int turnNumber;

    public NewTurnMessage(String activePlayerName, int turnNumber) {
        this.activePlayerName = activePlayerName;
        this.turnNumber = turnNumber;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller){
        controller.newTurn(activePlayerName, turnNumber);
    }
}
