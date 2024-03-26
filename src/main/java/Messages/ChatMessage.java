package main.java.Messages;

import main.java.Chat.Message;

public class ChatMessage implements GeneralMessage {
    private final Message message;

    public ChatMessage(Message message){
        this.message = message;
    }

    public void printData(){
        System.out.println("Chat Message: " + message.getMessage());
    }
}
