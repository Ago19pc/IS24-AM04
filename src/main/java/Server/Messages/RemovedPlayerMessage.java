package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class RemovedPlayerMessage implements ToClientMessage, Serializable {
    private final String name;

    public RemovedPlayerMessage(String name) {
        this.name = name;
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.playerRemoved(name);
    }
}
