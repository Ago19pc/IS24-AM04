package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class is responsible for receiving messages from the server and executing them on a socket connection.
 */
public class ClientReceiver extends Thread {
    private final ObjectInputStream in;

    private final ClientController controller;

    /**
     * Constructor
     * @param clientSocket the socket to receive messages from
     * @param controller the controller to use. This is used to execute incoming messages
     * @throws IOException if the socket can't be opened
     * @throws IllegalArgumentException if the socket is not open. This is used to prevent the RMI port from being used which would deadlock the client
     */
    public ClientReceiver(Socket clientSocket, ClientController controller) throws IOException, IllegalArgumentException {
        this.controller = controller;
        if(clientSocket.getInputStream().available() == 0){
            throw new IllegalArgumentException("This is not an open socket. Make sure this is not the RMI port");
        }
        in = new ObjectInputStream(clientSocket.getInputStream());
    }


    /**
     * Receives messages from the server and executes them
     */
    @Override
    public void run() {
        while (true) {
            ToClientMessage packet;

            try {
                packet = (ToClientMessage) in.readObject();
                packet.clientExecute(this.controller);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server Disconnected");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
