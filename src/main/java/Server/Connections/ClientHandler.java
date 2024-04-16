package Server.Connections;

import ConnectionUtils.Receiver;
import ConnectionUtils.Sender;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {
    private final Socket socket;

    private final Sender sender ;
    private final Receiver receiver ;
    private final Thread.UncaughtExceptionHandler h;
    public ClientHandler(ConnectionHandler connectionHandler,Socket client) throws IOException, RuntimeException {

        this.socket = client;
        h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, Throwable ex){
                System.out.println("AGAGAG");
                //Thread.currentThread().interrupt();
                throw new RuntimeException("Error dioboia", ex);
            }
        };
        try {
            sender = new Sender(this.socket);
            receiver = new Receiver(this.socket);
            sender.setUncaughtExceptionHandler(h);
            sender.start();
        } catch (Exception e) {
            System.out.println("LOL2");
            throw e;
        }
    }

    public void run() {
        //throw new RuntimeException("EHOLA");
        /*
        System.out.println("Client connected: " + this.socket.getInetAddress());
        try {

            receiver.setUncaughtExceptionHandler(h);
            receiver.start();

            receiver.join();
            sender.join();
            
        } catch (Exception e) {
            System.out.println("LOL");
            try {
                throw e;
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }


        }
        */


    }

    public void sendMessages(){
        sender.sendMessage();
    }



}
