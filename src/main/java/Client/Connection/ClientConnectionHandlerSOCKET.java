package Client.Connection;

import Client.Controller.ClientController;
import ConnectionUtils.MessagePacket;
import ConnectionUtils.MessageUtils;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientConnectionHandlerSOCKET extends Thread implements ClientConnectionHandler {
    private Socket clientSocket;
    private MessageUtils messageUtils = new MessageUtils();
    public ClientSender sender;
    public ClientReceiver receiver;
    private ClientController controller;


    /**
     * Constructor
     * Establishes a connection with the server
     */
    public ClientConnectionHandlerSOCKET(ClientController controller)  {
        this.controller = controller;
        this.sender = new ClientSender(this, controller);
    }

    /**
     * Constructor for the tests, sets the host to localhost and port to 1234
     */
    public ClientConnectionHandlerSOCKET(boolean debugMode, ClientController controller) {
        try {
            this.controller = controller;
            clientSocket = new Socket("localhost", 1234);
            sender = new ClientSender(this, controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * @param clientSocket the socket
     * @throws IOException
     */
    public void setSocket(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        try {
            this.sender.setOutputBuffer(new PrintWriter(clientSocket.getOutputStream(), true));
            this.receiver = new ClientReceiver(this, clientSocket, controller);
            Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread th, Throwable ex) {
                    System.out.println("Uncaught exception: " + ex);
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
     *
     * @param msg String to send
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException {
        sender.sendMessage(msg);
    }

    /**
     * Sends a message to the server
     * @param message the message to send
     * @throws IOException
     */
    public void sendMessage(GeneralMessage message) throws IOException {
        MessagePacket packet = new MessagePacket(message);
        sender.sendMessage(packet.stringify());

    }


    /**
     * Stops the connection, interrupts the sender and receiver threads and closes the socket
     * @throws IOException
     */
    public void stopConnection() throws IOException {
        receiver.interrupt();
        clientSocket.close();
    }

    public void executeMessage(GeneralMessage message) {
        try {
            message.clientExecute(controller);
        } catch (ClientExecuteNotCallableException e) {
            System.out.println("The received message should not be executed on the client");
            System.out.println("Message not executed");
        }
    }


}
