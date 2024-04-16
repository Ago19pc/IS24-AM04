package Server.Messages;

public class GameOverMessage implements GeneralMessage {

    @Override
    public void printData() {
        System.out.println("Game Over!");
    }

    public boolean equals(GeneralMessage other){
        System.out.println("GameOverMessage equals still to be implemented.");
        return true;
    }

}
