package Server.Messages;

import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.MessageType;

import java.io.Serializable;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private String name;
    private boolean confirmation;

    public PlayerNameMessage(String name, Boolean confirmation) {
        this.name = name;
        this.confirmation = confirmation;
    }

    @Override
    public void serverExecute(Controller controller) {

        for (ClientHandler c: controller.getConnectionHandler().getThreads()) {
            if (c.getReceiver().threadId() == Thread.currentThread().threadId()) {
                try {
                    controller.getConnectionHandler().setName(c, this.name);
                    controller.addPlayer(this.name);
                    System.out.println(c.getSocketAddress() + " ha scelto il nome " + this.name);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    PlayerNameMessage playerNameMessage = new PlayerNameMessage(name,false);
                    c.sendMessages(playerNameMessage);
                }
            }
        }
    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setName(confirmation);
    }
}