package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class NameNotYetSetMessage implements Serializable, ToClientMessage{
    public NameNotYetSetMessage() {
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.nameNotYetSet();
    }
}
