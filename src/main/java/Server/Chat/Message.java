package Server.Chat;

import java.io.Serializable;

/**
 * This class is used to represent messages
 *
 * @param message The message text
 * @param name    The name of the sender
 */
public record Message(String message, String name) implements Serializable {
    /**
     * Generates the message
     *
     * @param message the message text
     * @param name    the name of the sender
     */
    public Message {
    }

    /**
     * Returns the message text
     *
     * @return the text
     */
    @Override
    public String message() {
        return message;
    }

    /**
     * Returns the sender
     *
     * @return the sender name
     */
    @Override
    public String name() {
        return name;
    }
}
