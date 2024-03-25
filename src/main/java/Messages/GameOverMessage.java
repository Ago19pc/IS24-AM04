package main.java.Messages;

public class GameOverMessage implements GeneralMessage {

    @Override
    public void printData() {
        System.out.println("Game Over!");
    }
}
