package Server.Messages;

import java.io.Serializable;

public class GameNotYetStartedMessage implements Serializable, ToClientMessage {
    public GameNotYetStartedMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.gameNotYetStarted();
    }
}
