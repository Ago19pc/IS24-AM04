package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * This message is used to inform the client that the end game phase has started
 */
public class EndGamePhaseMessage implements Serializable, ToClientMessage {
    /**
     * Constructor
     */
    public EndGamePhaseMessage() {
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setEndGame();
    }
}
