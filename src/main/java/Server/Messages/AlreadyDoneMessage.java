package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;

import java.io.Serializable;

public class AlreadyDoneMessage implements Serializable, ToClientMessage {
    private final Actions action;
    public AlreadyDoneMessage(Actions action) {
        this.action = action;
    }
    @Override
    public void clientExecute(ClientController controller) {
        controller.alreadyDone(action);
    }
}
