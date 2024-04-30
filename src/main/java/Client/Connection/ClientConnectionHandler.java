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
    }


    public ClientConnectionHandler(boolean debugMode, ClientController controller) {
        try {
            this.controller = controller;
            clientSocket = new Socket("localhost", 1234);
            sender = new ClientSender(this, controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void setSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        try {
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
}
