package main.java.Chat;

import main.java.Player.Player;

import java.util.List;
import java.sql.Timestamp;

public class Chat {
    private List<Message> messages;

    public Chat(){
        System.out.println(this.toString() + "Chat created");
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
     * @param sender the player who sent the message
     */
    public void addMessage(String message, Player sender) {
        Message m = new Message(message, sender);
        messages.add(m);
    }

    /**
     * @return List<Message> the messages
     */
    public List<Message> getMessages() {
        return messages;
    }
}
