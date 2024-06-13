package Server.Connections;



import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used to send messages to a single socket client
 */
public class ServerSender {
    private ObjectOutputStream out;

    public ServerSender(Socket clientSocket) throws IOException {
        out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Sends a message packet to the client
     * @param message, the message packet to be sent
     */
    public void sendMessage(ToClientMessage message){
        try{
            out.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error sending message to client");
            e.printStackTrace();
        }
    }
}
