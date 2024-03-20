package main.java.Chat;

import java.util.List;

public class Chat {
    private List<Message> messages;

    public Chat(){
        System.out.println(this.toString() + "Chat created");
    }

    /**
     * Add a message to the chat
     * @param message
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * @return List<Message> the messages
     */
    public List<Message> getMessages() {
        return messages;
    }
}
