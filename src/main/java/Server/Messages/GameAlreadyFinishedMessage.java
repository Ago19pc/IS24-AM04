package Server.Messages;

import java.io.Serializable;

public class GameAlreadyFinishedMessage implements Serializable, ToClientMessage{
    public GameAlreadyFinishedMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller){
        controller.gameAlreadyFinished();
    }
}
