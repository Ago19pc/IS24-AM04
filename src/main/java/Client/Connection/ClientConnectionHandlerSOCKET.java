package Client.Connection;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.ToClientMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
/**
 * This class handles the connection between the client and the server using Socket.
 * It uses a ClientSender to send messages to the server and a ClientReceiver to receive messages from the server.
 */
public class ClientConnectionHandlerSOCKET extends Thread implements ClientConnectionHandler {
    private Socket clientSocket;
    public ClientSender sender;
    public ClientReceiver receiver;
    private ClientController controller;


    /**
     * Constructor
     * Starts attempting to establish a connection with the server
     * @param controller the controller to use. This is used to execute incoming messages
     */
    public ClientConnectionHandlerSOCKET(ClientController controller)  {
        this.controller = controller;
        this.sender = new ClientSender();
        this.start();
    }

    /**
     * Fast constructor. Used for testing purposes
     * @param debugMode if true, connects to localhost:1234 automatically
     * @param controller the controller to use. This is used to execute incoming messages
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
     * Connects to a server
     * @param host the ip address of the server
     * @param port the port of the server
     */
    public void setSocket(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        try {
            this.sender.setOutputBuffer(new ObjectOutputStream(clientSocket.getOutputStream()));
            this.receiver = new ClientReceiver(clientSocket, controller);
            Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread th, Throwable ex) {
                    System.out.println("Uncaught exception: " + ex);
                    ex.printStackTrace();
                    try {
                        clientSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            receiver.setUncaughtExceptionHandler(h);
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    //todo: add javadoc
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

    public void sendMessage(ToServerMessage message) throws IOException {
        sender.sendMessage(message);
    }


    /**
     * Stops the connection, interrupts the sender and receiver threads and closes the socket
     */
    public void stopConnection() throws IOException {
        receiver.interrupt();
        clientSocket.close();
    }

    public void executeMessage(ToClientMessage message) {
        message.clientExecute(controller);
    }


    /**
     * @return false because this is not an RMI connection
     */
    @Override
    public boolean ping() throws RemoteException {
        return false;
    }


}
