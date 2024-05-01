package Server.Connections;

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

    private ServerConnectionHandlerSOCKET serverConnectionHandler;


    public ServerReceiver(ClientHandler clientHandler, Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.serverConnectionHandler = clientHandler.getServerConnectionHandler();
        this.messageUtils = new MessageUtils(serverConnectionHandler);
    }

    /**
     * Run method for the thread
     * This method reads the messages and executes them
     */
    @Override
    public void run() {
        while (true) {
            MessagePacket packet;

            try {
                String resp = in.readLine();
                packet = new MessagePacket(resp);
                serverConnectionHandler.executeMessage(packet.getPayload());
            } catch (NullPointerException e) {
                try {
                    clientSocket.close();
                    serverConnectionHandler.killClient(clientHandler);
                    System.out.println("Client disconnesso ");
                    System.out.println("Ora i client connessi sono: ");
                    for (ClientHandler c : serverConnectionHandler.getThreads()) {
                        System.out.print(" " + c.getSocketAddress() + " -");
                    }
                    System.out.println();
                    break;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    throw new RuntimeException(e2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
