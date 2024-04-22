package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import ConnectionUtils.MessagePacket;
import ConnectionUtils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReceiver extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private ClientHandler clientHandler;
    private MessageUtils messageUtils;

    private ServerConnectionHandler serverConnectionHandler;


    public ServerReceiver(ClientHandler clientHanlder, Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHanlder;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.serverConnectionHandler = clientHanlder.getServerConnectionHandler();
        this.messageUtils = new MessageUtils(serverConnectionHandler);
    }


    /**
     * Run method for the thread
     * This method reads the messages from the server and calls the demuxe them to apply the correct event
     */
    @Override
    public void run() {
        while (true) {
            try {
                String resp = in.readLine();
                System.out.println(resp);
                MessagePacket packet = new MessagePacket(resp);
                packet.getPayload().serverExecute(serverConnectionHandler.getController());



            } catch (Exception e) {
                System.out.println("MIIIINCHIA");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
