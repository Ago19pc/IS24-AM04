package Client.Connection;

import ConnectionUtils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread {
    private Socket clientSocket;
    private MessageUtils messageUtils = new MessageUtils();
    private BufferedReader in;
    private ClientConnectionHandler clientConnectionHandler;


    public ClientReceiver(ClientConnectionHandler clientConnectionHandler, Socket clientSocket) throws IOException {
        this.clientConnectionHandler = clientConnectionHandler;
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    /**
     * Run method
     * Receives messages from the server and acts accordingly
     */
    @Override
    public void run() {
        while (true) {
            try {
                String resp = in.readLine();
                
                System.out.println(resp);
                // Capisci il tipo di messaggio dal prefisso (serve a scegliere il tipo di evento)
                //MessageUtils messageUtils = new MessageUtils();
                //messageUtils.client_demux(resp);



            } catch (Exception e) {
                System.out.println("Server Disconnected");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
