package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class OtherPlayerReconnectionMessage implements Serializable, ToClientMessage {
private String name;
    public OtherPlayerReconnectionMessage(String name){
        this.name = name;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.otherPlayerReconnected(name);
    }
}
