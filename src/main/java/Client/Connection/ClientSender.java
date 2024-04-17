package Client.Connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread {
    private Socket clientSocket;
    private Scanner in = new Scanner(System.in);
    private PrintWriter out;
    private ClientConnectionHandler clientConnectionHandler;

    public ClientSender(ClientConnectionHandler clientConnectionHandler, Socket clientSocket) throws IOException {
        this.clientConnectionHandler = clientConnectionHandler;
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
     * Sends a message to the server.
     * @param message String, the message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Sends a message to the server, which is read from the console.
     */
    public void sendMessage() {
        String message = in.nextLine();
        out.println(message);
    }

    /**
     * Body of the thread. What it should do
     */
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
