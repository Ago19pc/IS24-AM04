package Server.Chat;

import Server.Player.Player;

import java.util.LinkedList;
import java.util.List;

public class Chat {
    private final List<Message> messages;

    public Chat(){
        this.messages = new LinkedList<>();
        System.out.println(this.toString() + "Chat created");
    }

    /**
     * Add a message to the chat
     * @param message the text of the message
     * @param sender the player who sent the message
     */
    public void addMessage(String message, Player sender) throws IllegalArgumentException{
        boolean onlySpaces = true;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) != ' ') {
                onlySpaces = false;
                break;
            }
        }
        if(onlySpaces){
            throw new IllegalArgumentException("Message cannot be empty");
        }
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
