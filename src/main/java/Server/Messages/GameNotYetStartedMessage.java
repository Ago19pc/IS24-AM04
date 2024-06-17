package Server.Messages;

import java.io.Serializable;
/**
 * Message to inform the client that he cannot do what he tried to do because the game hasn't yet started
 */
public class GameNotYetStartedMessage implements Serializable, ToClientMessage {
    public GameNotYetStartedMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.gameNotYetStarted();
    }
}
