package Server.Connections;


import Server.Messages.ToServerMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ClientHandler clientHandler;

    private ServerConnectionHandlerSOCKET serverConnectionHandler;


    public ServerReceiver(ClientHandler clientHandler, Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.serverConnectionHandler = clientHandler.getServerConnectionHandler();
    }


    /**
     * Run method for the thread
     * This method reads the messages and executes them
     */
    @Override
    public void run() {
        while (true) {
            ToServerMessage packet;

            try {
                packet = (ToServerMessage) in.readObject();
                serverConnectionHandler.executeMessage(packet);
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
