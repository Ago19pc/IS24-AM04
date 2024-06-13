package Server.Chat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class is used to represent messages
 */
public class Message implements Serializable {
    private final String message;
    private final String name;
    private final Timestamp timestamp;

    /**
     * Generates the message
     * @param message the message text
     * @param name the name of the sender
     */
    public Message(String message, String name) {
        this.message = message;
        this.name = name;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Returns the message text
     * @return the text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the sender
     * @return the sender name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the timestamp
     * @return the timestamp of the message
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
