package Server.Messages;

import Server.Chat.Message;

public class ChatMessage implements Serializable, GeneralMessage {
    private final Message message;

    public ChatMessage(Message message){
        this.message = message;
    }

    public void printData(){
        System.out.println("Chat Message: " + message.getMessage());
    }

    public Message getMessage(){
        return message;
    }
    public boolean equals(GeneralMessage other){
        System.out.println("ChatMessage equals still to be implemented.");
        return this.message.equals(((ChatMessage) other).getMessage());
    }

}
