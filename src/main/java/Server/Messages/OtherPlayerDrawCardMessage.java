package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;
import java.util.List;

public class OtherPlayerDrawCardMessage implements Serializable, ToClientMessage {

    private final String name;
    private final Decks deckFrom;
    private final DeckPosition drawPosition;
    private final List<Card> newBoardCards;

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
