package Server.Chat;

import Server.Player.Player;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Chat implements Serializable {
    private final List<Message> messages;

    public Chat(){
        this.messages = new LinkedList<>();
    }

    /**
     * Add a message to the chat
     * @param message Message object to add to the list
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Add a message to the chat
     * @param message the text of the message
     * @param player the player who sent the message
     */
    public void addMessage(String message, Player player) {
        Message m = new Message(message, player.getName());
        messages.add(m);
    }


    public void addMessage(String message, String name) {
        Message m = new Message(message, name);
        messages.add(m);
    }

    /**
     * @return List<Message> the messages
     */
    public List<Message> getMessages() {
        return messages;
    }
}
