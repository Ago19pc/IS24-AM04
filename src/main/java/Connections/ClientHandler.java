package main.java.Connections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    public ClientHandler(Socket client){
        this.socket = client;
    }

    public void run() {
        System.out.println("Client connected: " + this.socket.getInetAddress());
    }

    public void sendMessages(){}

    public void receiveMessages(){}
}
