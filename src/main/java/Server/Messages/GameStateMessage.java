package Server.Messages;

import Server.GameModel.GameModel;

public class GameStateMessage implements GeneralMessage {

    GameModel gameModel;

    public GameStateMessage(GameModel gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void printData() {
        System.out.println("Game State: " + gameModel.toString());
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public boolean equals(GeneralMessage other){
        System.out.println("GameStateMessage equals still to be implemented.");
        return this.gameModel.equals(((GameStateMessage) other).getGameModel());
    }
}
