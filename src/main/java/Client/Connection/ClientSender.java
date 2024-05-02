package Client.Connection;

import Client.Controller.ClientController;

import java.io.PrintWriter;

public class ClientSender{

    private PrintWriter out;
    private ClientConnectionHandlerSOCKET clientConnectionHandler;
    private ClientController controller;

    public ClientSender(ClientConnectionHandlerSOCKET clientConnectionHandler, ClientController controller) {
        this.clientConnectionHandler = clientConnectionHandler;
        this.controller = controller;
    }
    /**
     * Sets this sender's output buffer
     * @param out the output buffer
     */
    public void setOutputBuffer(PrintWriter out) {
        this.out = out;
    }

    /**
     * Gets this sender's output buffer
     * @return the output buffer
     */
    public PrintWriter getOutputBuffer() {
        return out;
    }

    /**
     * Sends a message to the server.
     * @param message String, the message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /*private void cli_implementation(String[] args) {
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
                if(controller.getMyColor() != null){

                    System.out.println("You already chose a color");
                }
                if (args.length != 2) {
                    System.out.println("Correct usage: setColor <color RED|YELLOW|BLUE|GREEN>");
                    return;
                }
                controller.askSetColor(args[1]);
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
    }*/
}
