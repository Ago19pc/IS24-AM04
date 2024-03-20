package main.java.Connections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    BufferedReader in;
    PrintWriter out;
    public ClientHandler(Socket client){
        this.socket = client;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
    }

    public void run() {
        System.out.println("Client connected: " + this.socket.getInetAddress());
    }

    public void sendMessages(){}

    public void receiveMessages(){}
}
