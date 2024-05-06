package Client.Connection;

import Client.Controller.ClientController;
import ConnectionUtils.MessageUtils;
import ConnectionUtils.ToClientMessagePacket;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.IllegalMessageTypeException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread {
    private Socket clientSocket;
    private MessageUtils messageUtils = new MessageUtils();
    private BufferedReader in;
    private ClientConnectionHandlerSOCKET clientConnectionHandler;

    private ClientController controller;


    public ClientReceiver(ClientConnectionHandlerSOCKET clientConnectionHandler, Socket clientSocket, ClientController controller) throws IOException {
        this.clientConnectionHandler = clientConnectionHandler;
        this.clientSocket = clientSocket;
        this.controller = controller;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    /**
     * Run method
     * Receives messages from the server and acts accordingly
     */
    @Override
    public void run() {
        while (true) {
            ToClientMessagePacket packet;

            try {
                String resp = in.readLine();
                try {
                    packet = new ToClientMessagePacket(resp);
                    try {
                        packet.getPayload().clientExecute(this.controller);
                    } catch (ClientExecuteNotCallableException e) {
                        System.out.println("Called client execution when should not be done");
                    } catch (PlayerNotFoundByNameException e) {
                        System.out.println("ERROR: Player not found by name");
                    }
                } catch (ClassNotFoundException | IllegalMessageTypeException e) {
                    System.out.println("Errore pacchetto");
                    throw new RuntimeException(e);
                }





            } catch (IOException e) {
                System.out.println("Server Disconnected");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
