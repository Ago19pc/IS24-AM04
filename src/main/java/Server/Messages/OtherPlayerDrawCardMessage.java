package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;

import java.io.Serializable;
import java.util.List;

/**
 * Message to inform the client that another player has drawn a card
 */
public class OtherPlayerDrawCardMessage implements Serializable, ToClientMessage {
    /**
     * The name of the player who drew the card
     */
    private final String name;
    /**
     * The deck from which the card was drawn
     */
    private final Decks deckFrom;
    /**
     * The position in which the card was drawn
     */
    private final DeckPosition drawPosition;
    /**
     * The new board cards of the deck from which the card was drawn
     */
    private final List<Card> newBoardCards;
    /**
     * Constructor
     * @param name the name of the player who drew the card
     * @param deckFrom the deck from which the card was drawn
     * @param drawPosition the position in which the card was drawn
     * @param newBoardCards the new board cards
     */
    public OtherPlayerDrawCardMessage(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards) {
        this.name = name;
        this.deckFrom = deckFrom;
        this.drawPosition = drawPosition;
        this.newBoardCards = newBoardCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.drawOtherPlayer(name, deckFrom, drawPosition, newBoardCards);
    }
}
