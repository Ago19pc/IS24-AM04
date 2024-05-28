package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Card.GoldCard;
import Server.Card.ResourceCard;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;
import java.util.List;

public class StartGameMessage implements Serializable, ToClientMessage {
    private final List<GoldCard> goldBoardCards;
    private final List<ResourceCard> resourceBoardCards;

    public StartGameMessage(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards) {
        this.goldBoardCards = goldBoardCards;
        this.resourceBoardCards = resourceBoardCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.startGame(goldBoardCards, resourceBoardCards);
    }
}
