package Server.Connections;


import Server.Controller.Controller;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.net.Socket;

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
     */
    public ServerConnectionHandlerSOCKET getServerConnectionHandler() {
        return this.connectionHandler;
    }

    /**
     * Returns the receiver of this client
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

    public int getSocketPort() {
        return this.socket.getPort();
    }


    public boolean isOnline() {return this.socket.isConnected();}
    }

