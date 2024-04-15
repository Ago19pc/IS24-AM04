package Server.Connections;

import ConnectionUtils.Receiver;
import ConnectionUtils.Sender;
import Server.EventManager.EventManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {
    private final Socket socket;

    Sender sender ;
    Receiver receiver ;
    String s;
    public ClientHandler(Socket client, EventManager eventManager) throws IOException {
        this.socket = client;
        sender = new Sender(this.socket);
        receiver = new Receiver(this.socket, eventManager);
        sender.start();
    }

    public void run() {

        System.out.println("Client connected: " + this.socket.getInetAddress());
        try {
            Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread th, Throwable ex) {
                    System.out.println("Uncaught exception: " + ex);
                    try {
                        socket.close();
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

    public void sendMessages(){
        sender.sendMessage();
    }



}
