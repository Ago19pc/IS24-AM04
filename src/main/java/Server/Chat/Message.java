package Server.Chat;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private final String message;
    private final String name;
    private final Timestamp timestamp;

    /**
     * Generates the message
     * @param message
     * @param name
     */
    public Message(String message, String name) {
        this.message = message;
        this.name = name;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return String the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Player the sender
     */
    public String getName() {
        return name;
    }

    /**
     * @return Timestamp the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
