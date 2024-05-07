package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import Client.Connection.ClientConnectionHandlerRMI;
import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Exception.TooManyPlayersException;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static java.rmi.server.RemoteServer.getClientHost;

/**
 * This class handles the connection between the server and the clients using RMI
 * The ServerConnectionHandlerRMI port is set to 1099
 */
public class ServerConnectionHandlerRMI implements ServerConnectionHandler {

    ServerConnectionHandler stub;
    Registry registry;

    /**
     * String is the ID
     */
    Map<String, ClientConnectionHandler> clients = new HashMap<>();

    Controller controller;


    private int port;

    public ServerConnectionHandlerRMI() {
        startServer(1099);
        /*
        askForPort();
        while (!startServer(port)) {
            System.out.println("[RMI] Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("[RMI] Server avviato sulla porta: " + port);
        */

    }

    public ServerConnectionHandlerRMI(boolean debugMode) {
        if (debugMode) {
            port = 1235;
        } else {
            askForPort();
        }
        while (!startServer(port)) {
            System.out.println("[RMI] Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("[RMI] Server avviato sulla porta: " + port);
    }

    private boolean startServer(int port) {
        try {
            stub = (ServerConnectionHandler) UnicastRemoteObject.exportObject((ServerConnectionHandler) this, port);
            registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServerConnectionHandler", stub);
            System.out.println("[RMI] Server avviato sulla porta: " + port);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("[RMI] Errore durante l'avvio del server RMI");
            return false;
        }
    }

    private void askForPort() {
        System.out.println("[RMI] Inserire la porta su cui avviare il server: ");
        Scanner scanner = new Scanner(System.in);
        port = scanner.nextInt();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Get the stub for RMI
     * @return the stub of RMI
     */
    public ServerConnectionHandler getStub() {
        return stub;
    }

    /**
     * Send a message to all the clients
     *
     * @param message the message to send
     */
    @Override
    public void sendAllMessage(ToClientMessage message) {
        for (ClientConnectionHandler client : clients.values()) {
            try {
                client.executeMessage(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Send a message to a specific client
     * @param id the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(ToClientMessage message, String id) {
        try {
            clients.get(id).executeMessage(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a message
     *
     * @param message the message to execute
     */
    @Override
    public void executeMessage(ToServerMessage message) {
        synchronized (controller) {
            try {
                message.serverExecute(controller);
            } catch (ServerExecuteNotCallableException e) {
                System.out.println("This message should not be executed by the server");
                System.out.println("Message not executed!");
            } catch (PlayerNotFoundByNameException e) {
                System.out.println("Player not found by name");
                System.out.println("Message not executed!");
            }
            controller.notifyAll();
        }
    }

    /**
     * Kill a client
     *
     * @param id the id of the client to kill
     */
    @Override
    public void killClient(String id) {
        clients.remove(id);
    }

    @Override
    public void setName(String name, String clientID) {
        try {
            controller.addPlayer(name, clientID);
        } catch (TooManyPlayersException e) {
            throw new RuntimeException(e);
        }
    }

    public String join(int rmi_port) throws RemoteException, NotBoundException {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        String id = rand.nextInt(9999) + "-" + rand.nextInt(9999) + "-" + rand.nextInt(9999);
        Registry clientRegistry = null;
        try {
            clientRegistry = LocateRegistry.getRegistry(getClientHost(), rmi_port);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }
        ClientConnectionHandler client = (ClientConnectionHandler) clientRegistry.lookup("ClientConnectionHandler");
        clients.put(id, client);
        System.out.println("TEST IN SCHRMI, client connected");
        return id;
    }



    /**
     * Checks for an association between an id and a ClientConnectionHandler
     * @param id
     * @return
     */
    public boolean isClientAvailable(String id) {
        return clients.containsKey(id);
    }
}
