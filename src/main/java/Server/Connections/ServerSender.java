package Server.Connections;

import ConnectionUtils.MessagePacket;
import Server.Controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerSender extends Thread {
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
        System.out.println("Type help for a list of commands");
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendMessage(MessagePacket mp) throws IOException {
        out.println(mp.stringify());
    }


    @Override
    public void run() {
        while (true) {
            try {
                //sendMessage();
                cli_implementation(in.nextLine().split(" "));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cli_implementation(String[] args) {
        switch (args[0]) {
            case "help":
                System.out.println("Commands:");
                System.out.println("help");
                System.out.println("print");
                break;
            case "print":
                controller.printData();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
