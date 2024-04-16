package ConnectionUtils;

import Server.Enums.EventType;
import Server.Messages.PlayersMessage;
import Server.Player.Player;
import ConnectionUtils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import static Server.Enums.EventType.PLAYERSDATA;

public class Receiver extends Thread {
    private Socket clientSocket;

    private BufferedReader in;


    public Receiver(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }



    @Override
    public void run() {
        while (true) {
            try {
                String resp = in.readLine();
                
                System.out.println(resp);
                // Capisci il tipo di messaggio dal prefisso (serve a scegliere il tipo di evento)
                MessageUtils messageUtils = new MessageUtils();
                messageUtils.server_demux(resp);



            } catch (Exception e) {
                System.out.println("MIIIINCHIA");
                throw new RuntimeException("Error reading from socket", e);
            }

        }
    }
}
