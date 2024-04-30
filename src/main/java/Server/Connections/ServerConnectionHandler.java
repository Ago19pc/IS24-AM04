package Server.Connections;


import Server.Controller.Controller;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerConnectionHandler extends Thread {
    private ServerSocket socket;
    private Map<ClientHandler, String> clients;
    private int port;

    private Controller controller;



    /**
     * Create the server socket
     *
     */
    public ServerConnectionHandler() throws IOException {
        this.clients = new HashMap<>();
        askForPort();
        while (!startServer(port)) {
            System.out.println("Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("Server avviato sulla porta: " + port);
    }

    /**
     * Create the server socket
     * @param debugMode if true the server will start on port 1234
     */
    public ServerConnectionHandler(boolean debugMode){
        this.clients = new HashMap<>();
        if(debugMode)
            this.port = 1234;
        else
            askForPort();
        while (!startServer(port)) {
            System.out.println("Porta già in uso, provo la successiva...");
            port++;
        }
        System.out.println("Server avviato sulla porta: " + port);
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
                System.out.println("ServerConnnectionHandler gestore eccezioni thread figli: " + ex);
                th.interrupt();
            }
        };
        try {
            while (true) {
                Socket client = this.socket.accept();
                System.out.println("Nuova connessione accettata");
                ClientHandler t = new ClientHandler(this, client, controller);
                clients.put(t, null);
                t.setUncaughtExceptionHandler(h);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EDDIO SOLO SA PERCHE");
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
        System.out.println("Ciao, su quale porta vuoi avviare il server?");
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
            c.sendMessages(message);
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
        target.sendMessages(message);
    }

    /**
     * Sets a name for the ClientHandler to be easier to find later
     * @param clientHandler the ClientHandler to associate the name with
     * @param name the name to associate with the ClientHandler
     */
    public void setName(ClientHandler clientHandler, String name) {
        clients.put(clientHandler, name);
    }
}

