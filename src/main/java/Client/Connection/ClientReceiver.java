package Client.Connection;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientReceiver extends Thread {
    private ObjectInputStream in;

    private ClientController controller;


    public ClientReceiver(Socket clientSocket, ClientController controller) throws IOException {
        this.controller = controller;
        in = new ObjectInputStream(clientSocket.getInputStream());
    }


    /**
     * Run method
     * Receives messages from the server and acts accordingly
     */
    @Override
    public void run() {
        while (true) {
            ToClientMessage packet;

            try {
                packet = (ToClientMessage) in.readObject();

                try {
                    packet.clientExecute(this.controller);
                } catch (ClientExecuteNotCallableException e) {
                    System.out.println("Called client execution when should not be done");
                } catch (PlayerNotFoundByNameException e) {
                    System.out.println("ERROR: Player not found by name");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server Disconnected");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
