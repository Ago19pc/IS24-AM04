package Server.Connections;


import ConnectionUtils.MessagePacket;
import Server.Controller.Controller;
import Server.Enums.MessageType;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final ServerConnectionHandler connectionHandler;
    private final ServerSender sender ;
    private final ServerReceiver receiver ;
    private final Thread.UncaughtExceptionHandler h;
    private Controller controller;


    ClientHandler me = this;
    public ClientHandler(ServerConnectionHandler connectionHandler, Socket client, Controller controller) throws IOException, RuntimeException {
        this.controller = controller;
        this.socket = client;
        this.connectionHandler = connectionHandler;
        h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, Throwable ex){
                System.out.println("Exception, killing ClientHandler Thread " + ex);
                connectionHandler.killClient(me);
            }
        };
        try {
            sender = new ServerSender(this, this.socket, this.controller);
            receiver = new ServerReceiver(this, this.socket);
            sender.setUncaughtExceptionHandler(h);
            sender.start();
        } catch (Exception e) {
            System.out.println("LOL2");
            throw e;
        }
    }

    public void run() {
        System.out.println("Client connected: " + this.socket.getInetAddress());

        receiver.setUncaughtExceptionHandler(h);
        receiver.start();

        try {
            receiver.join();
            sender.join();
        } catch (InterruptedException e) {
            System.out.println("ClientHandler caught an exception");
            throw new RuntimeException(e);
        }


    }



    public void sendMessages(MessageType type, GeneralMessage message)  {
        MessagePacket mp = new MessagePacket(message, type);
        try {
            this.sender.sendMessage(mp.stringify());
        } catch (IOException e) {
            System.out.println("Unable to stringify chat message");
            throw new RuntimeException(e);
        }
    }


    public ServerConnectionHandler getServerConnectionHandler() {
        return this.connectionHandler;
    }

    public ServerReceiver getReceiver() {
        return this.receiver;
    }

}
