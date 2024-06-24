package Server.Chat;

import Server.Player.Player;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Chat class to store messages
 */
public class Chat implements Serializable {
    /**
     * List of messages in the chat
     */
    private final List<Message> messages;
    /**
     * Constructor. Creates an empty list of messages
     */
    public Chat(){
        this.messages = new LinkedList<>();
    }

    /**
     * Add a message object to the chat
     * @param message Message object to add to the list
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Add a message to the chat using the text and the sender
     * @param message the text of the message
     * @param player the player who sent the message
     */
    public void addMessage(String message, Player player) {
        Message m = new Message(message, player.getName());
        messages.add(m);
    }

    /**
     * Get the list of messages
     * @return the messages as a list
     */
    public List<Message> getMessages() {
        return messages;
    }
}
