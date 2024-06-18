package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * This message is used to inform the client that he needs to set his color before doing what he tried to do
 */
public class ColorNotYetSetMessage implements Serializable, ToClientMessage {

    public ColorNotYetSetMessage() {
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.colorNotYetSet();
    }
}
