package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.GoldCard;
import Server.Card.ResourceCard;

import java.io.Serializable;
import java.util.List;

/**
 * Message to send the client the starting deck data
 */
public class StartGameMessage implements Serializable, ToClientMessage {
    /**
     * The gold board cards. The first card is the top card of the deck, the other cards are the board cards
     */
    private final List<GoldCard> goldBoardCards;
    /**
     * The resource board cards. The first card is the top card of the deck, the other cards are the board cards
     */
    private final List<ResourceCard> resourceBoardCards;

    /**
     * Constructor
     * @param goldBoardCards the gold board cards. The first card is the top card of the deck, the other cards are the board cards
     * @param resourceBoardCards the resource board cards. The first card is the top card of the deck, the other cards are the board cards
     */
    public StartGameMessage(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards) {
        this.goldBoardCards = goldBoardCards;
        this.resourceBoardCards = resourceBoardCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.startGame(goldBoardCards, resourceBoardCards);
    }
}
