package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Enums.GameState;
import Server.Exception.*;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;
import Server.Player.Player;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.NotBoundException;
import java.rmi.Remote;
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
public class ServerConnectionHandlerRMI implements ServerConnectionHandler, Remote {

    ServerConnectionHandler stub;
    Registry registry;

    /**
     * String is the ID
     */
    final Map<String, ClientConnectionHandler> clients = new HashMap<>();

    Controller controller;

    private int port;

    public ServerConnectionHandlerRMI() {
        startServer(1099);
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

    /**
     * Starts the server on the specified port.
     * If the port is not available will try the following one, and so on until it finds one.
     * If non is found will return false.
     * @param port the port to start the server on
     * @return true if started successfully
     */

    private boolean startServer(int port) {
        try {
            String ip;
            try { //todo. @ago19 questa è una soliuzione temporanea per ottenere l'ip del server
                // Reply by ago: it works fine, the teacher said it's the only way so its ok. Also I can't find anything better that will work all the time.
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    // filters out 127.0.0.1 and inactive interfaces
                    if (iface.isLoopback() || !iface.isUp())
                        continue;

                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while(addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        ip = addr.getHostAddress();
                        if(ip.contains(":")) continue;
                        System.setProperty("java.rmi.server.hostname", ip);
                        System.out.println(iface.getDisplayName() + " " + ip);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println("Server hostname is " + System.getProperty("java.rmi.server.hostname"));
            stub = (ServerConnectionHandler) UnicastRemoteObject.exportObject( this, port);
            registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServerConnectionHandler", stub);
            System.out.println("[RMI] Server avviato sulla porta: " + port);
            return true;
        } catch (RemoteException e) {
            System.err.println("[RMI] Errore durante l'avvio del server RMI");
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

    public List<String> getAllIds(){
        return clients.keySet().stream().toList();
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
        pingAll();
        for (ClientConnectionHandler client : clients.values()) {
            try {
                String clientId = clients.entrySet().stream().filter(entry -> entry.getValue().equals(client)).findFirst().get().getKey();
                if(!controller.getConnectionHandler().isInDisconnectedList(clientId)){
                    client.executeMessage(message);
                }
            } catch (RemoteException e) {
                System.out.println(" Send All Message : Client disconnected: " + clients.entrySet().stream().filter(entry -> entry.getValue().equals(client)).findFirst().get().getKey());
            }
        }
    }

    /**
     * Send a message to a specific client
     * @param id the name of the client to send the message to
     * @param message the message to send
     */
    public void sendMessage(ToClientMessage message, String id) {
        ping(id);
        try{
            clients.get(id).executeMessage(message);
        } catch (RemoteException e) {
            System.out.println("Send message : Client disconnected");
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
            message.serverExecute(controller);
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
        System.out.println("Client killed. Sending message");

    }

    @Override
    public void setName(String name, String clientID) throws IllegalArgumentException, TooManyPlayersException, AlreadyStartedException {
        controller.addPlayer(name, clientID);
    }

    public LobbyPlayersMessage join(int rmi_port) throws RemoteException, NotBoundException {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        String id = rand.nextInt(9999) + "-" + rand.nextInt(9999) + "-" + rand.nextInt(9999);
        Registry clientRegistry;
        try {
            clientRegistry = LocateRegistry.getRegistry(getClientHost(), rmi_port);

            ClientConnectionHandler client = (ClientConnectionHandler) clientRegistry.lookup("ClientConnectionHandler");
            clients.put(id, client);
            System.out.println("[RMI] Client connected " + getClientHost());
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }
        //immediately send the lobby players message
        Map<String, Color> playerColors = new HashMap<>();
        System.out.println(controller.getPlayerList());
        controller.getPlayerList().forEach((p) -> {
            System.out.println(p);
            System.out.println(p.getName() + " " + p.getColor());
            playerColors.put(p.getName(), p.getColor());
        });
        Map<String, Boolean> playerReady = new HashMap<>();
        controller.getPlayerList().forEach(p -> playerReady.put(p.getName(), p.isReady()));
        Boolean isSavedGame = controller.getGameState().equals(GameState.LOAD_GAME_LOBBY);
        return new LobbyPlayersMessage(
                controller.getPlayerList().stream().map(Player::getName).toList(),
                playerColors,
                playerReady,
                id,
                isSavedGame
        );
    }

    /**
     * Pings a client. If it fails, sets the client as offline
     * @param id the id of the client to check
     */
    public void ping(String id) {
        System.out.println("Pinging " + id);
        try {
           clients.get(id).ping();
        } catch (RemoteException e) {
            System.out.println("ClientIDs are " + clients.keySet());
            System.out.println("Failed to ping " + id + ". Setting offline");
            controller.setOffline(id);
        }
    }

    /**
     * Pings all clients and sets the disconnected ones as offline
     */
    public void pingAll() {
        Map<String, ClientConnectionHandler> allClients = new HashMap<>(clients);
        allClients = allClients.entrySet().stream().filter(entry -> !controller.getConnectionHandler().isInDisconnectedList(entry.getKey())).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
        System.out.println("Pinging all clients. They are: " + allClients.keySet());
        allClients.keySet().forEach(this::ping);
    }

    /**
     * Checks for an association between an id and a ClientConnectionHandler
     * @param id the id of the client
     * @return true if client id is present
     */
    public boolean isClientAvailable(String id) {
        return clients.containsKey(id);
    }

    public void changeId(String oldId, String newId) {
        clients.remove(oldId);
    }
}
