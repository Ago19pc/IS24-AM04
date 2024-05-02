package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import Client.Connection.ClientConnectionHandlerRMI;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Messages.GeneralMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.rmi.server.RemoteServer.getClientHost;

/**
 * This class handles the connection between the server and the clients using RMI
 * The ServerConnectionHandlerRMI port is set to 1099
 */
public class ServerConnectionHandlerRMI implements ServerConnectionHandler {

    ServerConnectionHandler stub;
    Registry registry;

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
            System.setProperty("java.rmi.server.hostname","localhost");
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
    public void sendAllMessage(GeneralMessage message) {
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
     * @param name the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(GeneralMessage message, String name) {
        try {
            clients.get(name).executeMessage(message);
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
    public void executeMessage(GeneralMessage message) {
        synchronized (controller) {
            try {
                message.serverExecute(controller);
            } catch (ServerExecuteNotCallableException e) {
                System.out.println("This message should not be executed by the server");
                System.out.println("Message not executed!");
            }
            controller.notifyAll();
        }
    }

    /**
     * Kill a client
     *
     * @param name the name of the client to kill
     */
    @Override
    public void killClient(String name) {
        clients.remove(name);
    }

    @Override
    public void setName(String host, int port, String name) {
        try {
            Registry clientRegistry = LocateRegistry.getRegistry(host, port);
            ClientConnectionHandler client = (ClientConnectionHandler) registry.lookup("ClientConnectionHandler");
            clients.put(name, client);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks for an association between a name and a ClientConnectionHandler
     * @param name
     * @return
     */
    public boolean isClientNameAvailable(String name) {
        return clients.containsKey(name);
    }

    /**
     * Checks for an association between a [host, port] and a ClientConnectionHandler
     * This function is not implemented and should never be called
     * Maybe it's useless and the program can work fine without it.
     * Still thinking about it
     * @param host
     * @param port
     * @return
     */
    @Override
    public boolean isClientAddressAvailable(String host, int port) {
        System.out.println("isClientAddressAvailable function has not been implemented, and maybe it should never be");
        return false;
    }
}
