package Server.Connections;

import Server.EventManager.EventManager;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class ConnectionHandler extends Thread {
    private ServerSocket socket;
    private List<Thread> threads;

    private EventManager eventManager;


    /**
     * Create the server socket, needed to choose port
     *
     * @param port port to listen to for connections
     */
    public ConnectionHandler(int port, EventManager eventManager) throws IOException {
        this.eventManager = eventManager;
        threads = new ArrayList<>();
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
                Thread t = new ClientHandler(client, eventManager);

                t.setUncaughtExceptionHandler(h);

                t.start();
                threads.add(t);
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
    List<Thread> getThreads() {
        return this.threads;
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
}