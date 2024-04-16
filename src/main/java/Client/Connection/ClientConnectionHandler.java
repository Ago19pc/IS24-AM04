package Client.Connection;

import ConnectionUtils.MessagePacket;
import Server.Enums.EventType;
import Server.Messages.PlayerNameMessage;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionHandler {
    private Socket clientSocket;

    public ClientSender sender;
    public ClientReceiver receiver;


    public ClientConnectionHandler(String ip, int port) {
        try {
            startConnection(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void startConnection(String ip, int port) throws IOException, ClassNotFoundException {
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

        PlayerNameMessage pnm = new PlayerNameMessage("NOME1");
        MessagePacket message = new MessagePacket(pnm, EventType.PLAYERSDATA);
        //sender.sendMessage(message.stringify());
        MessagePacket test = new MessagePacket(message.stringify());
        System.out.println(test.equals(message));

        System.out.println(message.getType());
        System.out.println(test.getType());

        message.getPayload().printData();
        test.getPayload().printData();



    }

    public void sendMessage(String msg) throws IOException {
        sender.sendMessage(msg);
    }

    public void sendMessage() throws IOException {
        sender.sendMessage();
    }



    public void stopConnection() throws IOException {
        sender.interrupt();
        receiver.interrupt();
        clientSocket.close();
    }
}
