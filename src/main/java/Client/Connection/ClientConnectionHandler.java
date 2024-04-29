package Client.Connection;

import Client.Controller.ClientController;
import ConnectionUtils.MessagePacket;
import ConnectionUtils.MessageUtils;
import Server.Enums.MessageType;
import Server.Messages.GeneralMessage;
import Server.Messages.PlayerNameMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler extends Thread {
    private Socket clientSocket;
    private MessageUtils messageUtils = new MessageUtils();
    public ClientSender sender;
    public ClientReceiver receiver;
    private ClientController controller;


    /**
     * Constructor
     * Establishes a connection with the server
     */
    public ClientConnectionHandler(ClientController controller)  {
        this.controller = controller;
        this.sender = new ClientSender(this, controller);
        sender.start();
    }


    public ClientConnectionHandler(boolean debugMode, ClientController controller) {
        try {
            this.controller = controller;
            clientSocket = new Socket("localhost", 1234);
            sender = new ClientSender(this, controller);
            sender.debugSetSocket(clientSocket);
            receiver = new ClientReceiver(this, clientSocket);
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
                sender.setUncaughtExceptionHandler(h);

                sender.start();
                receiver.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void setSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.receiver = new ClientReceiver(this, clientSocket);
        try {
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

        try {
            sender.join();
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
     * Asks to input the string to send
     * @throws IOException
     */
    public void sendMessage() throws IOException {
        sender.sendMessage();
    }

    public void sendMessage(GeneralMessage message, MessageType type) throws IOException {
        MessagePacket packet = new MessagePacket(message, type);
        sender.sendMessage(packet.stringify());

    }


    /**
     * Stops the connection, interrupts the sender and receiver threads and closes the socket
     * @throws IOException
     */
    public void stopConnection() throws IOException {
        sender.interrupt();
        receiver.interrupt();
        clientSocket.close();
    }
}
