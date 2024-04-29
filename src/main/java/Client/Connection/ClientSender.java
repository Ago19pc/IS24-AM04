package Client.Connection;

import Client.Controller.ClientController;
import Server.Enums.Face;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
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

    public void debugSetSocket(Socket clientSocket) throws IOException {
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
                System.out.println("setColor <color RED|YELLOW|BLUE|GREEN>");
                System.out.println("setReady");
                System.out.println("chat <message>");
                System.out.println("play <hard_pos> <x> <y> <face FRONT | BACK>");
                break;
            case "join":
                if (args.length != 3) {
                    System.out.println("Correct usage: join <ip> <port>");
                    return;
                }
                try {
                    setSocket(args[1], Integer.parseInt(args[2]));
                } catch (IOException e) {
                    System.out.println("Could not connect to server");
                }
                break;
            case "setName":
                if(controller.getMyName() != null )
                {
                    System.out.println("You already have a name");
                    return;
                }
                if (args.length != 2) {
                    System.out.println("Correct usage: setName <name>");
                    return;
                }
                controller.askSetName(args[1]);
                break;
            case "setReady":
                controller.setReady();
                break;
            case "setColor":
                if (args.length != 2) {
                    System.out.println("Correct usage: setColor <color RED|YELLOW|BLUE|GREEN>");
                    return;
                }
                controller.setColor(args[1]);
                break;
            case "play":

                if(controller.getActivePlayerName() != controller.getMyName()){System.out.println("Wait for your turn"); return;}
                if (args.length != 5) {
                    System.out.println("Correct usage: play <hard_pos> <x> <y> <face FRONT | BACK>");
                    return;
                }
                Face face;
                try {
                    face = Face.valueOf(args[4].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid color, must be RED, YELLOW, BLUE or GREEN");
                    return;
                }
                controller.askForCardPlacement(Integer.parseInt(args[1]), face, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            case "chat":
                if(args.length == 1){
                    System.out.println("Correct usage: chat <message>");
                            return;
                }

                String regeneratedMessage = Arrays.stream(args).skip(1).reduce("", (s, e) -> s + e + " ");
                controller.sendChatMessage(regeneratedMessage);
                break;
            default:
                System.out.println("Invalid command");
        }
    }
}
