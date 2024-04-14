package it.am04.demo1.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread {
    private Socket clientSocket;

    private Scanner in;

    private PrintWriter out;

    public Sender(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new Scanner(System.in);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendMessage() {
        String message = in.nextLine();
        out.println(message);
    }
    @Override
    public void run() {
        while (true) {
            try {
                sendMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
