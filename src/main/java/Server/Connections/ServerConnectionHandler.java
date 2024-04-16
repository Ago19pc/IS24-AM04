package Server.Connections;



import Server.Controller.Controller;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ServerConnectionHandler extends Thread {
    private ServerSocket socket;
    private List<ClientHandler> clients;

    private int port;

    private Controller controller;



    /**
     * Create the server socket, needed to choose port
     *
     * @param controller controller instance
     */
    public ServerConnectionHandler(Controller controller) throws IOException {
        this.controller = controller;
        askForPort();

        clients = new ArrayList<>();
        while (!startServer(port)) {
            System.out.println("Port already in use, trying next port...");
            port++;
        }
        System.out.println("Server started on port: " + port);

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
                // QUI ANDREBBE GESTITO IL CASO DI RICONNESIONE
                //
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
                .filter(e -> e.getId() != (target.getId()))
                .collect(Collectors.toList());

    }

}
