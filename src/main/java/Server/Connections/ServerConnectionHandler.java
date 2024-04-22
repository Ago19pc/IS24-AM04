package Server.Connections;



import Server.Controller.Controller;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServerConnectionHandler extends Thread {
    private ServerSocket socket;
    private List<ClientHandler> clients;
    private Map<Long, String> clientNames;

    private int port;

    private Controller controller;



    /**
     * Create the server socket, needed to choose port
     *
     */
    public ServerConnectionHandler() throws IOException {
        this.clientNames = new HashMap<Long, String>();
        askForPort();

        clients = new ArrayList<>();
        while (!startServer(port)) {
            System.out.println("Port already in use, trying next port...");
            port++;
        }
        System.out.println("Server started on port: " + port);

    }

    public ServerConnectionHandler(boolean debugMode){
        this.clientNames = new HashMap<Long, String>();
        if(debugMode)
            this.port = 1234;
        else
            askForPort();
        clients = new ArrayList<>();
        while (!startServer(port)) {
            System.out.println("Port already in use, trying next port...");
            port++;
        }
        System.out.println("Server started on port: " + port);
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
                System.out.println("Uncaught exception: " + ex);
                th.interrupt();
                System.out.println("SMT");
            }
        };
        try {
            while (true) {
                Socket client = this.socket.accept();
                System.out.println("Received connection");
                ClientHandler t = new ClientHandler(this, client);
                t.setUncaughtExceptionHandler(h);
                t.start();
                clients.add(t);
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
    List<ClientHandler> getThreads() {
        return this.clients;
    }

    private boolean startServer(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void askForPort() {
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Hello my man, what port would you like to start your server on?");
        this.port = inputReader.nextInt();
    }

    public void killClient(ClientHandler target ) {
        target.interrupt();
        this.clients = this.clients.stream()
                .filter(e -> e.threadId() != (target.threadId()))
                .collect(Collectors.toList());
        //String/Player offlineplayer = target.getPlayer()
        //controller.setOffline(offlineplayer);
    }

    /**
     * Adds client name to the map of names and thread.
     * It also handles when a client reconnects with the same name, but the thread id is different.
     * @param threadID the id of the thread
     * @param name the name of the client
     */
    public void addClientName(Long threadID, String name) {
        if (clientNames.containsValue(name)){
            this.clientNames.put(threadID, name);
        } else {
            // get old id
            Long oldID = this.clientNames.entrySet().stream()
                    .filter(e -> e.getValue().equals(name))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            // replace old id with new id
            this.replaceID(oldID, threadID);

        }

    }

    public void removeClientName(Long threadID) {
        this.clientNames.remove(threadID);
    }

    private void replaceID(Long oldID, Long newID) {
        String name = this.clientNames.get(oldID);
        this.clientNames.remove(oldID);
        this.clientNames.put(newID, name);
        //controller.reconnectPlayer(name);
    }

}
