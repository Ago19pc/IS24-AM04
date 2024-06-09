package Server.Messages;

import java.io.Serializable;

public class ReconnectionNameMessage implements ToClientMessage, Serializable {
    private final String name;

    public ReconnectionNameMessage(String name) {
        this.name = name;
    }

    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.setName(name);
    }
}
