package Server.Messages;

import java.io.Serializable;

/**
 * Message to inform the client that he cannot do what he tried to do because the game has already finished
 */
public class GameAlreadyFinishedMessage implements Serializable, ToClientMessage{
    public GameAlreadyFinishedMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller){
        controller.gameAlreadyFinished();
    }
}
