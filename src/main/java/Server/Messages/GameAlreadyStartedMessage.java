package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class GameAlreadyStartedMessage implements Serializable, ToClientMessage {
    public GameAlreadyStartedMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.gameAlreadyStarted();
    }
}
