package main.java.Messages;

import main.java.GameModel.GameModel;

public class GameStateMessage implements GeneralMessage {

    GameModel gameModel;

    public GameStateMessage(GameModel gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void printData() {
        System.out.println("Game State: " + gameModel.toString());
    }
}
