package main.java.Connections;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler extends Thread {
    private ServerSocket socket;
    private List<Thread> threads;


    /**
     * Create the server socket, needed to choose port
     *
     * @param port port to listen to for connections
     */
    public ConnectionHandler(int port) throws IOException {
        threads = new ArrayList<>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket client = this.socket.accept();
                System.out.println("Client connected: " + client.getInetAddress());
                // QUI ANDREBBE GESTITO IL CASO DI RICONNESIONE
                Thread t = new ClientHandler(client);
                t.start();
                threads.add(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the client threads list
     * @return this.threads the list of all client thread
     */
    List<Thread> getThreads() {
        return this.threads;
    }

    /**
     *
     * @return the handler of all connection thread
     */



}