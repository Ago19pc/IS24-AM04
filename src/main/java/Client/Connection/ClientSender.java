package Client.Connection;

import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This class is responsible for sending messages to the server on a socket client.
 */

public class ClientSender{

    private ObjectOutputStream out;

    /**
     * Sets this sender's output buffer
     * @param out the output buffer
     */
    public void setOutputBuffer(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * Gets this sender's output buffer
     * @return the output buffer
     */
    public ObjectOutputStream getOutputBuffer() {
        return out;
    }

    /**
     * Sends a message to the server.
     * @param message the message to send.
     */
    public void sendMessage(ToServerMessage message) throws IOException {
        out.writeObject(message);
    }


}
