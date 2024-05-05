package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class NameNotYetSetMessage implements Serializable, ToClientMessage{
    public NameNotYetSetMessage() {
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.nameNotYetSet();
    }
}
