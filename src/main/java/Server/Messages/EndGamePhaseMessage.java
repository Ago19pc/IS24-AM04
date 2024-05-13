package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;

public class EndGamePhaseMessage implements Serializable, ToClientMessage {

    public EndGamePhaseMessage() {
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setEndGame();
    }
}
