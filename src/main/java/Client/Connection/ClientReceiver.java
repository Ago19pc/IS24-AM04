package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
     * @throws SocketTimeoutException if the socket times out (e.g. RMI port selected)
     */
    public ClientReceiver(Socket clientSocket, ClientController controller) throws IOException, SocketTimeoutException {
        this.controller = controller;
        clientSocket.setSoTimeout(10000);
        in = new ObjectInputStream(clientSocket.getInputStream());
        clientSocket.setSoTimeout(0);
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
                controller.serverDisconnected();
            }
        }
    }
}
