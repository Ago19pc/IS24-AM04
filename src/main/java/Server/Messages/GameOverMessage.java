package Server.Messages;

public class GameOverMessage implements GeneralMessage {

    @Override
    public void printData() {
        System.out.println("Game Over!");
    }
}
