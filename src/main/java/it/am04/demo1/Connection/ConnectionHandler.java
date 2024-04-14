package it.am04.demo1.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionHandler {
    private Socket clientSocket;

    public Sender sender;
    public Receiver receiver;

    public ConnectionHandler(String ip, int port) {
        try {
            startConnection(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Connection established");
        sender = new Sender(clientSocket);
        receiver = new Receiver(clientSocket);

        sender.start();
        receiver.start();

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
