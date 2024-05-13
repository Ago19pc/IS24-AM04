package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;
import java.util.List;

public class StartGameMessage implements Serializable, ToClientMessage {
    private final List<Card> goldBoardCards;
    private final List<Card> resourceBoardCards;

    public StartGameMessage(List<Card> goldBoardCards, List<Card> resourceBoardCards) {
        this.goldBoardCards = goldBoardCards;
        this.resourceBoardCards = resourceBoardCards;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException{
        controller.startGame(goldBoardCards, resourceBoardCards);
    }
}
