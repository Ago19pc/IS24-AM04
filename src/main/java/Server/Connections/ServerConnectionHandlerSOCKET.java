package Server.Connections;


import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Enums.GameState;
import Server.Exception.*;
import Server.Messages.*;
import Server.Player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This class handles the connection between the server and the clients using sockets
 */
public class ServerConnectionHandlerSOCKET extends Thread implements ServerConnectionHandler {
    private ServerSocket socket;
    /**
     * The String is the ID
     */
    private final Map<ClientHandler, String> clients;
    private int port;

    private Controller controller;



    /**
     * Create the server socket
     */
    public ServerConnectionHandlerSOCKET() {
        this.clients = new HashMap<>();
        askForPort();
        while (!startServer(port)) {
            System.out.println("[Socket] Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("[Socket] Server avviato sulla porta: " + port);
    }

    /**
     * Create the server socket
     * @param debugMode if true the server will start on port 1234
     */
    public ServerConnectionHandlerSOCKET(boolean debugMode){
        this.clients = new HashMap<>();
        if(debugMode)
            this.port = 1234;
        else
            askForPort();
        while (!startServer(port)) {
            System.out.println("[Socket] Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("[Socket] Server avviato sulla porta: " + port);
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Accept new connections and create a new ClientHandler thread for each one
     */
    public void run() {
        Thread.UncaughtExceptionHandler h = (th, ex) -> {
            System.out.println("[Socket] ServerConnectionHandler gestore eccezioni thread figli: " + ex);
            th.interrupt();
        };
        try {
            while (true) {
                Socket client = this.socket.accept();
                System.out.println("[Socket] Nuova connessione accettata");
                ClientHandler t = new ClientHandler(this, client, controller);
                Random rand = new Random();
                rand.setSeed(System.currentTimeMillis());
                String id = rand.nextInt(9999) + "-" + rand.nextInt(9999) + "-" + rand.nextInt(9999);
                clients.put(t, id);
                t.setUncaughtExceptionHandler(h);
                t.start();
                //immediately send the lobby players message
                Map<String, Color> playerColors = new HashMap<>();
                controller.getPlayerList().forEach(p -> playerColors.put(p.getName(), p.getColor()));
                Map<String, Boolean> playerReady = new HashMap<>();
                controller.getPlayerList().forEach(p -> playerReady.put(p.getName(), p.isReady()));
                Boolean isSavedGame = controller.getGameState().equals(GameState.LOAD_GAME_LOBBY);
                LobbyPlayersMessage message = new LobbyPlayersMessage(
                        controller.getPlayerList().stream().map(Player::getName).toList(),
                        playerColors,
                        playerReady,
                        id,
                        isSavedGame
                );
                t.sendMessage(message);
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("[Socket] Errore nel main loop del ServerConnectionHandlerSOCKET");
        }
    }

    /**
     * Get the client threads list
     * @return this.threads the list of all client thread
     */
    public List<ClientHandler> getConnectedThreads() {
        List<ClientHandler> connected = new ArrayList<>();
        for (ClientHandler c : clients.keySet()) {
            if(!controller.getConnectionHandler().isInDisconnectedList(clients.get(c))){
                connected.add(c);
            }
        }
        return connected;
    }

    /**
     * Gets a client thread name
     * @param clientHandler the client thread
     * @return the name of the client thread
     */
    public String getThreadName(ClientHandler clientHandler) {
        return clients.get(clientHandler);
    }
    /**
     * Start the server on a specific port
     * @param port the port to start the server on
     * @return true if the server started correctly, false otherwise
     */
    private boolean startServer(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Ask for the port to start the server
     */
    private void askForPort() {
        Scanner inputReader = new Scanner(System.in);
        System.out.println("[Socket] Ciao, su quale porta vuoi avviare il server?");
        try {
            this.port = inputReader.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("[Socket] Inserisci un numero valido");
            askForPort();
        }
    }

    public void killClient(String id){
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(id))
                .toList().getFirst().getKey();
        clients.remove(target);
        target.interrupt();
    }

    public List<String> getAllIds(){
        return clients.values().stream().toList();
    }


    public Controller getController() {return this.controller;}


    public void sendAllMessage(ToClientMessage message) {
        for (ClientHandler c : clients.keySet()) {
            if(c.isClosed()) continue;
            String clientId = clients.get(c);
            if(!controller.getConnectionHandler().isInDisconnectedList(clientId)){
                c.sendMessage(message);
            }
        }
    }


    public void sendMessage(ToClientMessage message, String id) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), id))
                .toList().getFirst().getKey();
        target.sendMessage(message);
    }



    public void executeMessage(ToServerMessage message) {
        synchronized (controller) {
            message.serverExecute(controller);
            controller.notifyAll();
        }
    }

    public boolean isClientAvailable(String id) {
        return clients.containsValue(id);
    }

    /**
     * @return null because this is used by RMI clients
     */
    public LobbyPlayersMessage join(int rmi_port) throws RemoteException {
        return null;
    }
}

