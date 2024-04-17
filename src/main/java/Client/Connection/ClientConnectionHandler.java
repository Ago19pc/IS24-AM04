package Client.Connection;

import ConnectionUtils.MessagePacket;
import ConnectionUtils.MessageUtils;
import Server.Enums.EventType;
import Server.Messages.PlayerNameMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler extends Thread {
    private Socket clientSocket;
    private MessageUtils messageUtils = new MessageUtils();
    public ClientSender sender;
    public ClientReceiver receiver;

    private Scanner inputReader = new Scanner(System.in);

    /**
     * Constructor
     * Establishes a connection with the server
     */
    public ClientConnectionHandler()  {
        try {
            establishConnection(inputReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes a connection with the server
     * @param inputReader the scanner to read input from
     */
    private void establishConnection(Scanner inputReader) {
        try {
            System.out.println("Inserisci l'indirizzo IP del server");
            String ip = inputReader.nextLine();
            System.out.println("Inserisci la porta del server");
            int port = inputReader.nextInt();
            clientSocket = new Socket(ip, port);
            System.out.println("Connection established");
            sender = new ClientSender(this, clientSocket);
            receiver = new ClientReceiver(this, clientSocket);
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
                sender.setUncaughtExceptionHandler(h);

                sender.start();
                receiver.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            establishConnection(inputReader);
        }
    }




    /**
     * Run method
     * The main body, what the thread does
     */
    public void run() {

        // TEST CODE
        PlayerNameMessage pnm = new PlayerNameMessage("NOME1");
        MessagePacket message = new MessagePacket(pnm, EventType.PLAYERNAME);
        //sender.sendMessage(message.stringify());
        MessagePacket test = null;
        try {
            test = new MessagePacket(message.stringify());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(test.equals(message));

        System.out.println(message.getType());
        System.out.println(test.getType());

        message.getPayload().printData();
        test.getPayload().printData();
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
