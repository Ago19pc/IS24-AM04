package Client.Connection;

import Client.Controller.ClientController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientSender extends Thread {
    private Socket clientSocket;
    private Scanner in = new Scanner(System.in);
    private PrintWriter out;
    private ClientConnectionHandler clientConnectionHandler;
    private ClientController controller;

    public ClientSender(ClientConnectionHandler clientConnectionHandler, ClientController controller) {
        this.clientConnectionHandler = clientConnectionHandler;
        this.controller = controller;
        System.out.println("Type help for a list of commands");
    }

    private void setSocket(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        clientConnectionHandler.setSocket(this.clientSocket);
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
                //sendMessage();
                String[] args = in.nextLine().split(" ");
                cli_implementation(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cli_implementation(String[] args) {
        if (!Objects.equals(args[0], "join") && !Objects.equals(args[0], "help") && clientSocket == null) {
            System.out.println("You need to join a server first");
            return;
        }
        switch (args[0]) {
            case "help":
                System.out.println("Commands:");
                System.out.println("help");
                System.out.println("join <ip> <port>");
                System.out.println("setName <name>");
                System.out.println("setReady");
                break;
            case "join":
                try {
                    setSocket(args[1], Integer.parseInt(args[2]));
                } catch (IOException e) {
                    System.out.println("Could not connect to server");
                }
                break;
            case "setName":
                controller.setName(args[1]);
                break;
            case "setReady":
                controller.setReady();
                break;
            default:
                System.out.println("Invalid command");
        }
    }
}
