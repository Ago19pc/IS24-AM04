package Server.Connections;


import Server.Controller.Controller;
import Server.Messages.ToClientMessage;

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
     * Create the server socket, needed to choose port
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

    /**
     * Gets the name of a client handler
     * @param ClientHandler the client handler
     * @return the player name
     */
    public String getThreadName(ClientHandler thread) {
        return clients.get(thread);
    }

    private boolean startServer(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void askForPort() {
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Ciao, su quale porta vuoi avviare il server?");
        this.port = inputReader.nextInt();
    }

    public void killClient(ClientHandler target ) {
        //target.interrupt();
        clients.remove(target);
        //String/Player offlineplayer = target.getPlayer()
        //controller.setOffline(offlineplayer);
    }

    public void killClient(String name) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .toList().getFirst().getKey();
        killClient(target);
    }

    public Controller getController() {return this.controller;}

    public void sendAllMessage(ToClientMessage message) {
        for (ClientHandler c : clients.keySet()) {
            System.out.println("Sending message to " + c.getSocketAddress());
            c.sendMessages(message);
        }
    }
    public void sendMessage(ToClientMessage message, String name) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue()!= null && entry.getValue().equals(name))
                .toList().getFirst().getKey();
        System.out.println("NAME MATCH FOUND");
        target.sendMessages(message);
    }

    public void setName(ClientHandler clientHandler, String name) {
        clients.put(clientHandler, name);
    }
}

