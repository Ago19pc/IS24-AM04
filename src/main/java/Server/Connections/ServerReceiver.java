package Server.Connections;

import Client.Connection.ClientConnectionHandler;
import ConnectionUtils.MessagePacket;
import ConnectionUtils.MessageUtils;
import Server.Chat.Message;
import Server.Exception.ServerExecuteNotCallableException;

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
     * This method reads the messages and executes them
     */
    @Override
    public void run() {
        while (true) {
            MessagePacket packet;

            try {
                String resp = in.readLine();
                packet = new MessagePacket(resp);

                try {
                    synchronized (serverConnectionHandler.getController()) {
                        packet.getPayload().serverExecute(serverConnectionHandler.getController());
                        System.out.println("RECEIVED MESSAGE AND EXECUTED");
                        serverConnectionHandler.getController().notifyAll();
                    }
                }
                catch (Exception e) {
                    System.out.println("ServerReciver: Error with synconized block");
                }


            } catch (Exception e) {
                System.out.println("Error reading from socket");
                e.printStackTrace();
                throw new RuntimeException("Error reading from socket", e);
            }


        }
    }
}
