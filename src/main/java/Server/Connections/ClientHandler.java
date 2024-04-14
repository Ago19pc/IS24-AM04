package Server.Connections;

import ConnectionUtils.Receiver;
import ConnectionUtils.Sender;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {
    private final Socket socket;
    Sender sender ;
    Receiver receiver ;
    String s;
    public ClientHandler(Socket client) throws IOException {
        this.socket = client;
        sender = new Sender(this.socket);
        receiver = new Receiver(this.socket);
        sender.start();
    }

    public void run() {

        System.out.println("Client connected: " + this.socket.getInetAddress());
        try {
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendMessages(){
        sender.sendMessage();
    }



}
