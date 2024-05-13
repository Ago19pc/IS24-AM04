package Server.Connections;


import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Exception.TooManyPlayersException;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.PlayerDisconnectedMessage;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class ServerConnectionHandlerSOCKET extends Thread implements ServerConnectionHandler {
    private ServerSocket socket;
    /**
     * The String is the ID
     */
    private Map<ClientHandler, String> clients;
    private int port;

    private Controller controller;



    /**
     * Create the server socket
     */
    public ServerConnectionHandlerSOCKET() throws IOException {
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

    /**
     * Set the controller instance
     * @param controller the controller instance
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Accept new connections and create a new ClientHandler thread for each one
     */
    public void run() {
        Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, Throwable ex) {
                System.out.println("[Socket] ServerConnnectionHandler gestore eccezioni thread figli: " + ex);
                th.interrupt();
            }
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
                LobbyPlayersMessage message = new LobbyPlayersMessage(
                        controller.getPlayerList().stream().map(p -> p.getName()).toList(),
                        playerColors,
                        playerReady,
                        id
                );
                t.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Socket] EDDIO SOLO SA PERCHE");
        }
    }

    /**
     * Get the client threads list
     * @return this.threads the list of all client thread
     */
    public List<ClientHandler> getThreads() {
        return this.clients.keySet().stream().toList();
    }

    public String getThreadName(ClientHandler clientHandler) {
        return clients.get(clientHandler);
    }

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
        this.port = inputReader.nextInt();
    }

    /**
     * Kill a ClientHandler thread
     * @param id the name of the client thread to kill
     */
    public void killClient(String id) throws PlayerNotFoundByNameException {
        PlayerDisconnectedMessage message = new PlayerDisconnectedMessage(controller.getConnectionHandler().getPlayerNameByID(id));
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(id))
                .toList().getFirst().getKey();
        clients.remove(target);
        target.interrupt();
        controller.getConnectionHandler().sendAllMessage(message);
    }

    public List<String> getAllIds(){
        return clients.values().stream().toList();
    }


    /**
     * Get the controller instance
     * @return the ControllerInstance of the server
     */
    public Controller getController() {return this.controller;}

    /**
     * Send a message to all the clients
     * @param message the message to send
     */
    public void sendAllMessage(ToClientMessage message) {
        for (ClientHandler c : clients.keySet()) {
            System.out.println("Sending message to " + c.getSocketAddress());
            c.sendMessage(message);
        }
    }

    /**
     * Send a message to a specific client
     * @param message the message to send
     * @param id the name of the client to send the message to
     */
    public void sendMessage(ToClientMessage message, String id) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), id))
                .toList().getFirst().getKey();
        System.out.println("NAME MATCH FOUND");
        target.sendMessage(message);
    }

    /**
     * Sets a name for the ClientHandler to be easier to find later
     * @param id the id of the client to associate with the name
     * @param name the name
     */
    public void setName(String name, String id) {
        try {
            controller.addPlayer(name, id);
        } catch (TooManyPlayersException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a message
     * @param message, a GeneralMessage to be executed
     */
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

    public boolean isClientAvailable(String id) {
        return clients.containsValue(id);
    }

    public LobbyPlayersMessage join(int rmi_port) throws RemoteException {
        return null;
    }

}

