package Server.Connections;


import Server.Controller.Controller;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.PlayerNotFoundByNameException;
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
    private final Thread.UncaughtExceptionHandler h;
    private Controller controller;


    ClientHandler me = this;

    /**
     * Constructor
     * @param connectionHandler the ServerConnectionHandler that created this ClientHandler
     * @param client the client's socket
     * @param controller the controller of the server
     * @throws IOException if the sender or receiver can't be created
     * @throws RuntimeException if the sender or receiver can't be created
     */
    public ClientHandler(ServerConnectionHandlerSOCKET connectionHandler, Socket client, Controller controller) throws IOException, RuntimeException {
        this.controller = controller;
        this.socket = client;
        this.connectionHandler = connectionHandler;
        h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, Throwable ex){
                System.out.println("Exception, killing ClientHandler Thread " + ex);
                try {
                    connectionHandler.killClient(connectionHandler.getThreadName(me));
                } catch (PlayerNotFoundByNameException e) {
                    throw new RuntimeException(e);
                } catch (AlreadyFinishedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        try {
            sender = new ServerSender(this.socket);
            receiver = new ServerReceiver(this, this.socket, this.controller);
            receiver.setUncaughtExceptionHandler(h);
        } catch (Exception e) {
            System.out.println("LOL2");
            throw e;
        }
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
     * Returns the receiver of this client
     * @return the receiver
     */
    public ServerReceiver getReceiver() {
        return this.receiver;
    }

    /**
     * get this client's address
     * @return the client's address
     */
    public String getSocketAddress() {
        return this.socket.getInetAddress().getHostAddress();
    }

    /**
     * get this client's port
     * @return the client's port
     */
    public int getSocketPort() {
        return this.socket.getPort();
    }

    /**
     * checks if the client is online
     * @return true if the socket is connected, false otherwise
     */
    public boolean isOnline() {return this.socket.isConnected();}
    /**
     * checks if the client is closed
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {return this.socket.isClosed();}
    }

