package Server.Connections;


import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerConnectionHandlerSOCKET extends Thread implements ServerConnectionHandler {
    private ServerSocket socket;
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
                clients.put(t, null);
                t.setUncaughtExceptionHandler(h);
                t.start();
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
     * @param target the client thread to kill
     */
    public void killClient(ClientHandler target ) {
        //target.interrupt();
        clients.remove(target);
        //String/Player offlineplayer = target.getPlayer()
        //controller.setOffline(offlineplayer);
    }

    /**
     * Kill a ClientHandler thread
     * @param name the name of the client thread to kill
     */
    public void killClient(String name) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .toList().getFirst().getKey();
        killClient(target);
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
    public void sendAllMessage(GeneralMessage message) {
        for (ClientHandler c : clients.keySet()) {
            System.out.println("Sending message to " + c.getSocketAddress());
            c.sendMessage(message);
        }
    }

    /**
     * Send a message to a specific client
     * @param message the message to send
     * @param name the name of the client to send the message to
     */
    public void sendMessage(GeneralMessage message, String name) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .toList().getFirst().getKey();
        System.out.println("NAME MATCH FOUND");
        target.sendMessage(message);
    }

    /**
     * Sets a name for the ClientHandler to be easier to find later
     * @param host the ip of the client
     * @param port the port of the client
     * @param name the name to associate with the ClientHandler
     */
    public void setName(String host, int port, String name) {
        ClientHandler clientHandler = clients.keySet().stream()
                .filter(c -> c.getSocketAddress().equals(host) && c.getSocketPort() == port)
                .toList().getFirst();
        clients.put(clientHandler, name);
    }

    /**
     * Execute a message
     * @param message, a GeneralMessage to be executed
     */
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

    public boolean isClientNameAvailable(String name) {
        return clients.values().contains(name);
    }

    @Override
    public boolean isClientAddressAvailable(String host, int port) {
    return clients.keySet().stream()
                .anyMatch(c -> c.getSocketAddress().equals(host) && c.getSocketPort() == port);
    }

}

