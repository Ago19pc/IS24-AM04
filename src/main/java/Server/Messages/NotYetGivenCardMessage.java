package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class NotYetGivenCardMessage implements Serializable, ToClientMessage {
    private final Actions type;
    public NotYetGivenCardMessage(Actions type) {
        this.type = type;
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.notYetGivenCard(type);
    }
}
