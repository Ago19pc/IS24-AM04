package it.am04.demo1.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver extends Thread {
    private Socket clientSocket;

    private BufferedReader in;

    public Receiver(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String resp = in.readLine();
                System.out.println(resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
