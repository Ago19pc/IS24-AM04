package Server.Messages;

import java.io.Serializable;

public class NewTurnMessage implements Serializable, ToClientMessage{
    private String activePlayerName;
    private int turnNumber;

    public NewTurnMessage(String activePlayerName, int turnNumber) {
        this.activePlayerName = activePlayerName;
        this.turnNumber = turnNumber;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller) throws Server.Exception.PlayerNotFoundByNameException {
        controller.newTurn(activePlayerName, turnNumber);
    }
}
