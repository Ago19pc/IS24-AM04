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

public class PlayerNameMessage implements ToClientMessage, ToServerMessage, Serializable {
    private String name;
    private boolean confirmation;
    private String id;

    public PlayerNameMessage(String name, Boolean confirmation) {
        this.name = name;
        this.confirmation = confirmation;
    }

    public PlayerNameMessage(String name, Boolean confirmation, String id) {
        this.name = name;
        this.confirmation = confirmation;
        this.id = id;
    }

    public PlayerNameMessage(Boolean confirmation){this.confirmation = confirmation;}

    @Override
    public void serverExecute(Controller controller) {

        try {
            controller.getConnectionHandler().setName(this.name, this.id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.setName(confirmation);
    }
}