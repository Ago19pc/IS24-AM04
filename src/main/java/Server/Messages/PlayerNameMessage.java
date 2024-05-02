package Server.Messages;

import Client.Connection.ClientConnectionHandler;
import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Exception.TooManyElementsException;
import Server.Exception.TooManyPlayersException;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;

import static java.rmi.server.RemoteServer.getClientHost;

public class PlayerNameMessage implements GeneralMessage, Serializable {
    private String name;
    private boolean confirmation;

    public PlayerNameMessage(String name, Boolean confirmation) {
        this.name = name;
        this.confirmation = confirmation;
    }
    public PlayerNameMessage(Boolean confirmation){this.confirmation = confirmation;}

    @Override
    public void serverExecute(Controller controller) {

        for (ClientHandler c: controller.getConnectionHandler().getServerConnectionHandlerSOCKET().getThreads()) {

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
                return;
            }
        }

        try {
            controller.addPlayer(this.name);
            controller.getConnectionHandler().setName(getClientHost(), 1100, this.name);
            PlayerNameMessage playerNameMessage = new PlayerNameMessage(true);
            controller.getConnectionHandler().getServerConnectionHandlerRMI().sendMessage(playerNameMessage, this.name);
            NewPlayerMessage playerMessage = new NewPlayerMessage(controller.getPlayerList());
            controller.getConnectionHandler().sendAllMessage(playerMessage);
        } catch (IllegalArgumentException | TooManyPlayersException | ServerNotActiveException e) {

            try {
                PlayerNameMessage playerNameMessage = new PlayerNameMessage(false);
                Registry clientRegistry = LocateRegistry.getRegistry(getClientHost(), 1100);
                ClientConnectionHandler client = (ClientConnectionHandler) clientRegistry.lookup("ClientConnectionHandler");
                client.executeMessage(playerNameMessage);
            } catch (RemoteException | ServerNotActiveException | NotBoundException ex) {
                System.out.println("Cannot send INVALID NAME MESSAGE back to client");
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setName(confirmation);
    }
}