package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;
import java.util.List;

public class StartGameMessage implements Serializable, ToClientMessage {
    private final List<String> playerNames;
    private final List<Card> goldBoardCards;
    private final List<Card> resourceBoardCards;

    public StartGameMessage(List<String> playerNames, List<Card> goldBoardCards, List<Card> resourceBoardCards) {
        this.playerNames = playerNames;
        this.goldBoardCards = goldBoardCards;
        this.resourceBoardCards = resourceBoardCards;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.startGame(playerNames, goldBoardCards, resourceBoardCards);
    }
}
