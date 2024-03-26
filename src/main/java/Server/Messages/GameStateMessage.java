package main.java.Server.Messages;

import main.java.Server.GameModel.GameModel;

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
