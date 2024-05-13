package Server.Connections;



import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender {
    private ObjectOutputStream out;

    public ServerSender(Socket clientSocket) throws IOException {
        out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Sends a message packet to the client
     * @param message, the message packet to be sent
     * @throws IOException
     */
    public void sendMessage(ToClientMessage message) throws IOException {
        out.writeObject(message);
    }
}
