package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class ColorNotYetSetMessage implements Serializable, ToClientMessage {

    public ColorNotYetSetMessage() {
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.colorNotYetSet();
    }
}
