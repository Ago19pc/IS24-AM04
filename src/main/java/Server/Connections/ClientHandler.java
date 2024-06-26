package Server.Connections;


import Server.Controller.Controller;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * This class is used to represent socket clients in the server. It manages the using the sender and receiver classes.
 * It is created by the ServerConnectionHandlerSOCKET when a new client connects.
 * @see ServerSender
 * @see ServerReceiver
 */
public class ClientHandler extends Thread {
    private final Socket socket;
    private final ServerConnectionHandlerSOCKET connectionHandler;
    private final ServerSender sender ;
    private final ServerReceiver receiver ;


    /**
     * Constructor
     * @param connectionHandler the ServerConnectionHandler that created this ClientHandler
     * @param client the client's socket
     * @param controller the controller of the server
     * @throws IOException if the sender or receiver can't be created
     * @throws RuntimeException if the sender or receiver can't be created
     */
    public ClientHandler(ServerConnectionHandlerSOCKET connectionHandler, Socket client, Controller controller) throws IOException, RuntimeException {
        this.socket = client;
        this.connectionHandler = connectionHandler;
        UncaughtExceptionHandler h = (th, ex) -> {
            System.out.println("Exception, killing ClientHandler Thread " + ex);
            ex.printStackTrace();
            connectionHandler.killClient(connectionHandler.getThreadName(this));

        };
        sender = new ServerSender(this.socket);
        receiver = new ServerReceiver(this, this.socket, controller);
        receiver.setUncaughtExceptionHandler(h);
    }


    /**
     * Creates the receiver to listen to the client messages
     */
    public void run() {
        System.out.println("[SOCKET] Nuovo Client connesso: " + this.socket.getInetAddress());
        System.out.println("[SOCKET] Ora i client connessi sono: ");
        for (ClientHandler c : connectionHandler.getConnectedThreads()) {
            System.out.print(" " + c.socket.getInetAddress() +" -");
        }
        System.out.println();
        receiver.start();

        try {
            receiver.join();
        } catch (InterruptedException e) {
            System.out.println("ClientHandler caught an exception");
            throw new RuntimeException(e);
        }


    }


    /**
     * Sends a message to the client
     * @param message the message to send
     */
    public void sendMessage(ToClientMessage message)  {
        this.sender.sendMessage(message);
    }


    /**
     * Returns the associated ServerConnectionHandler
     * @return the ServerConnectionHandler
     */
    public ServerConnectionHandlerSOCKET getServerConnectionHandler() {
        return this.connectionHandler;
    }

    /**
     * get this client's address
     * @return the client's address
     */
    public String getSocketAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }
    /**
     * checks if the client is closed
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {return this.socket.isClosed();}
    }

