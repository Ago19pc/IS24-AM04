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

        for (ClientHandler c: controller.getConnectionHandler().getThreads()) {
            if (c.getReceiver().threadId() == Thread.currentThread().threadId()) {
                try {
                    controller.addPlayer(this.name);
                    controller.getConnectionHandler().setName(c, this.name);
                    PlayerNameMessage playerNameMessage = new PlayerNameMessage(true);
                    c.sendMessages(playerNameMessage);
                    NewPlayerMessage playerMessage = new NewPlayerMessage(controller.getPlayerList());
                    controller.getConnectionHandler().sendAllMessage(playerMessage);
                } catch (Exception e) {
                    PlayerNameMessage playerNameMessage = new PlayerNameMessage(false);
                    c.sendMessages(playerNameMessage);
                }
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