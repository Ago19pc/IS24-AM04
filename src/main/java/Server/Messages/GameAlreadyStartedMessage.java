package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;
/**
 * Message to inform the client that he cannot do what he tried to do because the game has already started
 */
public class GameAlreadyStartedMessage implements Serializable, ToClientMessage {
    public GameAlreadyStartedMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.gameAlreadyStarted();
    }
}
