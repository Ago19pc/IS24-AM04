package Server.Chat;

import java.sql.Timestamp;
import Server.Player.Player;

public class Message {
    private final String message;
    private final Player sender;
    private final Timestamp timestamp;

    /**
     * Generates the message
     * @param message
     * @param sender
     */
    public Message(String message, Player sender) {
        this.message = message;
        this.sender = sender;
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
    public Player getSender() {
        return sender;
    }

    /**
     * @return Timestamp the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
