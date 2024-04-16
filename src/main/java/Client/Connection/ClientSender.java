package Client.Connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread {
    private Socket clientSocket;

    private Scanner in;

    private PrintWriter out;

    private ClientConnectionHandler clientConnectionHandler;

    public ClientSender(ClientConnectionHandler clientConnectionHandler, Socket clientSocket) throws IOException {
        this.clientConnectionHandler = clientConnectionHandler;
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
