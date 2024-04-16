package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import ConnectionUtils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReceiver extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private ClientHandler clientHandler;


    public ServerReceiver(ClientHandler clientHanlder, Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHanlder;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }



    @Override
    public void run() {
        while (true) {
            try {
                String resp = in.readLine();

                System.out.println(resp);
                // Capisci il tipo di messaggio dal prefisso (serve a scegliere il tipo di evento)
                //MessageUtils messageUtils = new MessageUtils();
                //messageUtils.server_demux(resp);



            } catch (Exception e) {
                System.out.println("MIIIINCHIA");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
