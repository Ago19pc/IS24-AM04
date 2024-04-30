package Server.Messages;

import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;

import java.io.Serializable;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private String name;
    private boolean confirmation;

    public PlayerNameMessage(String name) {
        this.name = name;
    }
    public PlayerNameMessage(Boolean confirmation){this.confirmation = confirmation;}

    @Override
    public void serverExecute(Controller controller) {
        for (ClientHandler c: controller.getConnectionHandler().getClients()) {
            if (c.getReceiver().threadId() == Thread.currentThread().threadId()) {
                controller.addPlayer(this.name, c);
            }
        }
    }

    @Override
    public void clientExecute(ClientController controller) {

       if(confirmation) {
           controller.setName();
           System.out.println("Accepted name");
       }
       else {
           System.out.println("Invalid name, please choose another one");
       }

    }
}