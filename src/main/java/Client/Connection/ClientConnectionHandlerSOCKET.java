package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientConnectionHandlerSOCKET extends Thread implements ClientConnectionHandler {
    private Socket clientSocket;
    public ClientSender sender;
    public ClientReceiver receiver;
    private final ClientController controller;


    /**
     * Constructor
     * Establishes a connection with the server
     */
    public ClientConnectionHandlerSOCKET(ClientController controller)  {
        this.controller = controller;
        this.sender = new ClientSender();
        this.start();
    }

    /**
     * Constructor for the tests, sets the host to localhost and port to 1234
     */
    public ClientConnectionHandlerSOCKET(boolean debugMode, ClientController controller) {
        try {
            this.controller = controller;
            clientSocket = new Socket("localhost", 1234);
            sender = new ClientSender();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    /**
     * Gets socket output buffer
     * @return the socket output buffer
     */
    public OutputStream getSocketOutputBuffer() {
        try {
            return clientSocket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the socket for the receiver
     * @param host the ip address of the server
     * @param port the port of the server
     * @throws IOException
     */
    public void setSocket(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        try {
            this.sender.setOutputBuffer(new ObjectOutputStream(clientSocket.getOutputStream()));
            this.receiver = new ClientReceiver(clientSocket, controller);
            Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread th, Throwable ex) {
                    System.err.println("Uncaught exception: " + ex);
                    try {
                        clientSocket.close();

                    } catch (IOException e) {
                        System.err.println("Error while closing the socket");
                    }
                }
            };
            receiver.setUncaughtExceptionHandler(h);
            receiver.start();
        } catch (Exception e) {
            System.err.println("Error while setting the socket");
        }


    }



    /**
     * Run method
     * The main body, what the thread does
     */
    public void run() {
        while(receiver == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            receiver.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }




    /**
     * Sends a message to the server
     * @param message    e the message to send
     * @throws IOException
     */
    public void sendMessage(ToServerMessage message) throws IOException {
        sender.sendMessage(message);

    }


    /**
     * Stops the connection, interrupts the sender and receiver threads and closes the socket
     * @throws IOException
     */
    public void stopConnection() throws IOException {
        receiver.interrupt();
        clientSocket.close();
    }

    /**
     * Executes a message
     * @param message the message to execute
     */
    public void executeMessage(ToClientMessage message) {
        message.clientExecute(controller);
    }


    /**
     * This function is not used but is required by the interface
     * @return false
     * @throws RemoteException
     */
    @Override
    public boolean ping() throws RemoteException {
        return false;
    }


}
