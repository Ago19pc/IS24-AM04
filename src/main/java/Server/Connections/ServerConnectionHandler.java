package Server.Connections;


import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.MessageType;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

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
            System.out.println("Port already in use, trying next port...");
            port++;
        }
        System.out.println("Server started on port: " + port);

    }

    public ServerConnectionHandler(boolean debugMode){
        this.clients = new HashMap<>();
        if(debugMode)
            this.port = 1234;
        else
            askForPort();
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
                System.out.println("ServerConnnectionHandler gestore eccezioni thread figli: " + ex);
                th.interrupt();
            }
        };
        try {
            while (true) {
                Socket client = this.socket.accept();
                System.out.println("Received connection");
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

    public void sendAllMessage(GeneralMessage message, MessageType type) {
        for (ClientHandler c : clients.keySet()) {
            c.sendMessages(type, message);
        }
    }
    public void sendMessage(GeneralMessage message, MessageType type, String name) {
        ClientHandler target = clients.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .toList().getFirst().getKey();
        System.out.println("NAME MATCH FOUND");
        target.sendMessages(type, message);
    }

    public void setName(ClientHandler clientHandler, String name) {
        clients.put(clientHandler, name);
    }
}

