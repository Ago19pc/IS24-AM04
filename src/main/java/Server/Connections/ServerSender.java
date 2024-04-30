package Server.Connections;

import ConnectionUtils.MessagePacket;
import Server.Controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerSender {
    private Socket clientSocket;
    private Scanner in;
    private PrintWriter out;
    private ClientHandler clientHandler;
    private Controller controller;

    public ServerSender(ClientHandler clientHandler, Socket clientSocket, Controller controller) throws IOException {
        this.clientHandler = clientHandler;
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new Scanner(System.in);
        this.controller = controller;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendMessage(MessagePacket mp) throws IOException {
        out.println(mp.stringify());
    }
}
