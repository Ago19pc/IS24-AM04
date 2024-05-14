package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class EndGamePhaseMessage implements Serializable, ToClientMessage {

    public EndGamePhaseMessage() {
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setEndGame();
    }
}
